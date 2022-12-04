from flask import Flask, send_file, request, jsonify
from werkzeug.utils import secure_filename
import os
import subprocess

app = Flask(__name__)

@app.route("/request", methods=['POST'])
def postRequest():
    f = request.files['file']
    file_path = os.path.join("gifs", secure_filename(f.filename))
    file_path_wo_ext = os.path.splitext(file_path)[0]
    f.save(file_path)
    files = os.listdir("gifs")
    gifs = [file for file in files if file.endswith('.gif')]

    if os.path.exists(f'{file_path_wo_ext}.gif'):
        return jsonify({
            'res': gifs,
            'status': '409',
            'msg': 'Conflict: File already exists!',
            'no_of_gifs': len(gifs)
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
                'msg': 'Can not parse CAFF!',
                'no_of_gifs': len(gifs)
            })
        else:
            return jsonify({
                'res': gifs,
                'status': '200',
                'msg': 'Success uploading caff!',
                'no_of_gifs': len(gifs)
            })
    except Exception as e:
        os.remove(file_path)
        return jsonify({
            'res': e,
            'status': '400',
            'msg': 'Can not parse CAFF!',
            'no_of_gifs': len(gifs)
        })

@app.route("/request/list", methods=['GET'])
def getRequest():
    files = os.listdir("gifs")
    gifs = [file for file in files if file.endswith('.gif')]
    return jsonify({
        'res': gifs,
        'status': '200',
        'msg': 'Success getting all the gifs in storage!',
        'no_of_gifs': len(gifs)
    })

@app.route("/request/<caff>", methods=['GET'])
def getRequestCaff(caff):
    return send_file(f'gifs/{caff}')

@app.route("/request/<caff>/meta", methods=['GET'])
def getRequestCaffMeta(caff):
    file_path_wo_ext = os.path.splitext(f'gifs/{caff}')[0]
    return send_file(f'{file_path_wo_ext}.json')

@app.route('/request/<caff>', methods=['DELETE'])
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