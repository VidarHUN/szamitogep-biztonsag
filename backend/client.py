import os
import requests

def get_secret_message():
    response = requests.get("https://localhost:8443", verify="domain.crt")
    print(f"The secret message is {response.text}")

if __name__ == "__main__":
    get_secret_message()