from pki_helpers import generate_private_key, generate_public_key, generate_csr, sign_csr
from cryptography import x509
from cryptography.hazmat.backends import default_backend
from getpass import getpass
from cryptography.hazmat.primitives import serialization

private_key = generate_private_key("ca-private-key.pem", "secret_password")
ca_cert = generate_public_key(
    private_key,
    filename="ca-public-key.pem",
    country="HU",
    state="Budapest",
    locality="Budapest",
    org="hitmit",
    hostname="hitmit.com",
)
print(ca_cert)

server_private_key = generate_private_key("server-private-key.pem", "serverpassword")

generate_csr(
    server_private_key,
    filename="server-csr.pem",
    country="HU",
    state="Budapest",
    locality="Budapest",
    org="hitmit",
    alt_names=["localhost"],
    hostname="hitmit.com",
)


csr_file = open("server-csr.pem", "rb")
csr = x509.load_pem_x509_csr(csr_file.read(), default_backend())

ca_public_key_file = open("ca-public-key.pem", "rb")
ca_public_key = x509.load_pem_x509_certificate(
    ca_public_key_file.read(), default_backend()
)


ca_private_key_file = open("ca-private-key.pem", "rb")
# It will prompt a "secret_password"
ca_private_key = serialization.load_pem_private_key(
    ca_private_key_file.read(),
    getpass().encode("utf-8"),
    default_backend(),
)

sign_csr(csr, ca_public_key, ca_private_key, "server-public-key.crt")