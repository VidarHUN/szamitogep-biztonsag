from flask import Flask, send_file, request, jsonify, make_response
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import generate_password_hash,check_password_hash
from werkzeug.utils import secure_filename
from functools import wraps
import os
import uuid
import subprocess
import datetime
import jwt
import json

app = Flask(__name__)
app.config['SECRET_KEY'] = os.getenv('SECRET')
app.config['SQLALCHEMY_DATABASE_URI']= os.getenv('DBPATH')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

db = SQLAlchemy(app)

# Models

class Users(db.Model):
   id = db.Column(db.Integer, primary_key=True)
   public_id = db.Column(db.Integer)
   name = db.Column(db.String(50))
   email = db.Column(db.String(50))
   password = db.Column(db.String(50))
   admin = db.Column(db.Boolean)

class Comments(db.Model):
   id = db.Column(db.Integer, primary_key=True)
   user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
   author = db.Column(db.String(50), unique=True, nullable=False)
   gif = db.Column(db.String(50), nullable=False)
   gif_prize = db.Column(db.Integer)
   comment = db.Column(db.String(100))

with app.app_context():
    db.create_all()

def token_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        token = None
        if 'x-access-tokens' in request.headers:
            token = request.headers['x-access-tokens']

        if not token:
            return make_response('Unauthorized',  401,
                        {'Authentication': '"Token is invalid"'})
        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
            current_user = Users.query.filter_by(public_id=data['public_id']).first()
        except:
            return make_response('Unauthorized',  401,
                        {'Authentication': '"Token is invalid"'})
        print("DFASZ, Token")
        return f(current_user, *args, **kwargs)
    return decorator

# User handling
@app.route('/users/register', methods=['POST'])
def signup_user():
    data = request.get_json()
    hashed_password = generate_password_hash(data['password'], method='sha256')
    # TODO: Check if email is valid!
    new_user = Users(
                    public_id=str(uuid.uuid4()), name=data['name'],
                    password=hashed_password, email=data['email'], admin=data['admin']
                )
    db.session.add(new_user)
    db.session.commit()
    return jsonify({'message': 'registered successfully'})

#Server error when email is bad
@app.route('/users/login', methods=['POST'])
def login_user():
    auth = request.authorization
    if not auth or not auth.username or not auth.password:
        return make_response('could not verify', 401,
                            {'Authentication': 'login required"'})

    user = Users.query.filter_by(email=auth.username).first()
    if check_password_hash(user.password, auth.password):
        token = jwt.encode({
            'public_id' : user.public_id,
            'exp' : datetime.datetime.utcnow() + datetime.timedelta(minutes=45)
            }, app.config['SECRET_KEY'], "HS256")

        return jsonify({'token' : token})

    return make_response('could not verify',  401,
                        {'Authentication': '"login required"'})

@app.route('/users', methods=['GET'])
@token_required
def get_users(current_user):
    user_data = Users.query.filter_by(name=current_user.name).first()
    return jsonify({
        'public_id': user_data.public_id,
        'name': user_data.name,
        'password': user_data.password,
        'admin': user_data.admin,
        'email': user_data.email
    })


@app.route('/users/list', methods=['GET'])
@token_required
def get_users_list(current_user):
    if not current_user.admin:
        return make_response('Forbidden',  403,
                        {'Authentication': '"admin privilege required"'})

    user_data = Users.query.all()
    result = []   
    for user in user_data:   
       user_data = {}   
       user_data['public_id'] = user.public_id  
       user_data['username'] = user.name 
       user_data['password'] = user.password
       user_data['email'] = user.email 
       result.append(user_data) 

    return jsonify({
        'users': result
    })

@app.route('/users/modify', methods=['PUT'])
@token_required
def modify_user(current_user):
    # Get name from message body
    name = request.json['name']
    hashed_password = generate_password_hash(request.json['password'], method='sha256')
    email = request.json['email']
    # Update Users table
    user = Users.query.filter_by(name=current_user.name).first()
    user.name = name
    user.password = hashed_password
    user.email = email
    db.session.commit()
    return jsonify({'message': 'Modification was successful'})

@app.route('/users/<user_id>', methods=['DELETE'])
@token_required
def delete_user(current_user, user_id):
    if not current_user.admin:
        return make_response('Forbidden',  403,
                        {'Authentication': '"admin privilege required"'})
    user = Users.query.filter_by(public_id=user_id).first()
    if not user:
        return jsonify({'message': 'user does not exist'})

    db.session.delete(user)
    db.session.commit()
    return jsonify({'message': 'User deleted'})

# Comment handling
@app.route('/comments', methods=['POST'])
@token_required
def create_comment(current_user):
    data = request.get_json()

    new_comment = Comments(
            author=data['author'],
            gif=data['gif'], gif_prize=data['gif_prize'],
            user_id=current_user.id, comment=data['comment']
            )
    db.session.add(new_comment)
    db.session.commit()
    return jsonify({'message' : 'new comment added'})

@app.route('/comments/<gif>', methods=['GET'])
@token_required
def get_comment(current_user, gif):
    comments = Comments.query.filter_by(gif=gif).all()
    output = []
    for comment in comments:
        comment_data = {}
        comment_data['id'] = comment.id
        comment_data['author'] = comment.author
        comment_data['gif'] = comment.gif
        comment_data['gif_prize'] = comment.gif_prize
        comment_data['comment'] = comment.comment
        output.append(comment_data)

    return jsonify({'list_of_comments' : output})

@app.route('/comments/<comment_id>/<user_id>', methods=['PUT'])
@token_required
def update_comment(current_user, comment_id, user_id):
    if not current_user.admin:
        return make_response('Forbidden',  403,
                        {'Authentication': '"admin privilege required"'})
    comment = request.json['comment']

    comment_object = Comments.query.filter_by(id=comment_id, user_id=user_id).first()
    comment_object.comment = comment
    db.session.commit()
    return jsonify({'message': 'Modification was successful'})

@app.route('/comments/<comment_id>/<user_id>', methods=['DELETE'])
@token_required
def delete_comment(current_user, comment_id, user_id):
    if not current_user.admin:
        return make_response('Forbidden',  403,
                        {'Authentication': '"admin privilege required"'})
    comment = Comments.query.filter_by(id = comment_id, user_id = user_id).first()
    if not comment:
        return jsonify({'message': 'Comment does not exist'})

    db.session.delete(comment)
    db.session.commit()
    return jsonify({'message': 'Comment deleted'})

@app.route("/request", methods=['POST'])
@token_required
def postRequest(self):
    f = request.files['file']
    file_path = os.path.join("gifs", secure_filename(f.filename))
    file_path_wo_ext = os.path.splitext(file_path)[0]
    f.save(file_path)
    files = os.listdir("gifs")

    if os.path.exists(f'{file_path_wo_ext}.gif'):
        return jsonify({
            'status': '409',
            'msg': 'Conflict: File already exists!'
        })

    try:
        out = subprocess.run(['./parser', '-f', file_path, '-o', f'{file_path_wo_ext}.gif'], capture_output=True)
        os.remove(file_path)
        files = os.listdir("gifs")
        gifs = [file for file in files if file.endswith('.gif')]
        if out.stderr:
            return jsonify({
                'res': out.stderr,
                'status': '400',
                'msg': 'Can not parse CAFF!'
            })
        else:
            return jsonify({
                'status': '200',
                'msg': 'Success uploading caff!'
            })
    except Exception as e:
        os.remove(file_path)
        return jsonify({
            'res': e,
            'status': '400',
            'msg': 'Can not parse CAFF!'
        })

@app.route("/request/list", methods=['GET'])
@token_required
def getRequest(self):
    files = os.listdir("gifs")
    gifs = []
    for f in files:
        if f.endswith('.gif'):
            gif = {'image': f, 'title': ''}
            file_json = os.path.splitext(f)[0]+'.json'
            with open ('gifs/'+ file_json) as file:
                data = json.load(file)
                gif['title'] = data['caption']
            gifs.append(gif)
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success getting all the gifs in storage!',
        'no_of_gifs': len(gifs)
    })

@app.route("/request/<caff>", methods=['GET'])
@token_required
def getRequestCaff(current_user,caff):
    return send_file(f'gifs/{caff}')

@app.route("/request/<caff>/meta", methods=['GET'])
@token_required
def getRequestCaffMeta(caff):
    file_path_wo_ext = os.path.splitext(f'gifs/{caff}')[0]
    return send_file(f'{file_path_wo_ext}.json')

@app.route('/request/<caff>', methods=['DELETE'])
@token_required
def deleteRequestCaff(caff):
    file_path = os.path.join("gifs", secure_filename(caff))
    file_path_wo_ext = os.path.splitext(file_path)[0]
    os.remove(file_path)
    os.remove(f'{file_path_wo_ext}.json')
    files = os.listdir("gifs")
    gifs = [file for file in files if file.endswith('.gif')]
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success deleting gif!',
        'no_of_gifs': len(gifs)
    })

if  __name__ == '__main__':
    app.run(debug=True)