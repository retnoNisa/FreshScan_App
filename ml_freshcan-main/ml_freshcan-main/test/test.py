import requests

resp = requests.post("https://getprediction-fw3nhsnjsa-et.a.run.app", files={'file': open('carrot.jpg', 'rb')})

print(resp.json())
