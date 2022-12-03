from flask import Flask, send_file, render_template, request, jsonify
from werkzeug.utils import secure_filename
import os

app = Flask(__name__)

@app.route("/request", methods=['POST'])
def postRequest():
    f = request.files['file']
    f.save(os.path.join("gifs", secure_filename(f.filename)))
    gifs = os.listdir("gifs")
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success uploading gif!',
        'no_of_gifs': len(gifs)
    })

@app.route("/request/list", methods=['GET'])
def getRequest():
    gifs = os.listdir("gifs")
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success getting all the gifs in storage!',
        'no_of_gifs': len(gifs)
    })

@app.route("/request/<caff>", methods=['GET'])
def getRequestCaff(caff):
    return send_file(f'gifs/{caff}')

@app.route('/request/<caff>', methods=['DELETE'])
def deleteRequestCaff(caff):
    os.remove(os.path.join("gifs", secure_filename(caff)))
    gifs = os.listdir("gifs")
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success deleting gif!',
        'no_of_gifs': len(gifs)
    })