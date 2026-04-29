from flask import Flask, jsonify
import json
import os

app = Flask(__name__)

# Json file path
DATA_FILE = "transactions.json"

"""Read Json file"""
def load_data():

    if not os.path.exists(DATA_FILE):
        return "JSON file not found."

    with open(DATA_FILE, "r", encoding="utf-8") as f:
        return json.load(f)


@app.route("/data", methods=["GET"])
def get_all_data():
    data = load_data()
    return jsonify(data)


# Running on http://127.0.0.1:5000/data
if __name__ == "__main__":
    app.run(debug=True)