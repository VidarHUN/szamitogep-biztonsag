import os
import requests

def get_secret_message():
    response = requests.delete("https://localhost:8443/request/3.gif", verify="domain.crt")
    print(response.content)

def upload():
    files = {'file': open('../parser/caff_files/3.caff', 'rb')}
    response = requests.post("https://localhost:8443/request", verify="domain.crt", files=files,)
    print(response.content)

if __name__ == "__main__":
    get_secret_message()
    # upload()