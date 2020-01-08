import sys
from bottle import route, run, template

@route('/')
def index():
    return template("<h1>It works!</h1><p>{{version_info}}", version_info=sys.version_info)

@route('/hello/<name>')
def hello(name):
    return template('<b>Hello {{name}}</b>!', name=name)

run(host='0.0.0.0', port=8080)
