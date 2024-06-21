from flask import Flask, request, jsonify
import mysql.connector
import os
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

app = Flask(__name__)

# Function to get a database connection
def get_db_connection():
    return mysql.connector.connect(
        host=os.getenv('MYSQL_HOST'),
        user=os.getenv('MYSQL_USER'),
        password=os.getenv('MYSQL_PASSWORD'),
        database=os.getenv('MYSQL_DB')
    )

# Error handler for database errors
@app.errorhandler(mysql.connector.Error)
def handle_database_error(error):
    return jsonify({'message': 'Database error: ' + str(error)}), 500

# Get all images of vegetables
@app.route('/getimages', methods=['GET'])
def get_all_images():
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT image FROM vegetables")
            results = cursor.fetchall()

        if results:
            images = [row[0] for row in results]
            return jsonify({'images': images})
        else:
            return jsonify({'message': 'No vegetables found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving images: ' + str(e)}), 500
    finally:
        conn.close()

# Get image of a specific vegetable
@app.route('/getimage/<vegetable_name>', methods=['GET'])
def get_image(vegetable_name):
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT image FROM vegetables WHERE name = %s", (vegetable_name,))
            result = cursor.fetchone()

        if result:
            return jsonify({'image': result[0]})
        else:
            return jsonify({'message': 'Vegetable not found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving image: ' + str(e)}), 500
    finally:
        conn.close()

# Get details of a specific vegetable
@app.route('/getdetail/<vegetable_name>', methods=['GET'])
def get_detail(vegetable_name):
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM vegetables WHERE name = %s", (vegetable_name,))
            result = cursor.fetchone()

        if result:
            vegetable = {
                'id': result[0],
                'name': result[1],
                'image': result[2],
                'detail': result[3],
                'Step 1': result[4],
                'Step 2': result[5],
                'Step 3': result[6],
                'Step 4': result[7],
                'Step 5': result[8],
                'Step 6': result[9]
            }
            return jsonify({'vegetable': vegetable})
        else:
            return jsonify({'message': 'Vegetable not found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving vegetable details: ' + str(e)}), 500
    finally:
        conn.close()

# Get article by title
@app.route('/getarticle/<article_title>', methods=['GET'])
def get_article(article_title):
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT image, title, detail, source FROM articles WHERE title = %s", (article_title,))
            result = cursor.fetchone()

        if result:
            article = {
                'image': result[0],
                'title': result[1],
                'detail': result[2],
                'source': result[3]
            }
            return jsonify({'article': article})
        else:
            return jsonify({'message': 'Article not found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving article: ' + str(e)}), 500
    finally:
        conn.close()

# Get all vegetables
@app.route('/getallvegetables', methods=['GET'])
def get_all_vegetables():
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM vegetables")
            results = cursor.fetchall()

        if results:
            vegetables = []
            for row in results:
                vegetable = {
                    'id': row[0],
                    'name': row[1],
                    'image': row[2],
                    'detail': row[3]
                }
                vegetables.append(vegetable)
            return jsonify({'vegetables': vegetables})
        else:
            return jsonify({'message': 'No vegetables found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving vegetables: ' + str(e)}), 500
    finally:
        conn.close()

# Get all articles
@app.route('/getallarticles', methods=['GET'])
def get_all_articles():
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM articles")
            results = cursor.fetchall()

        if results:
            articles = []
            for row in results:
                article = {
                    'id': row[0],
                    'image': row[1],
                    'title': row[2],
                    'detail': row[3],
                    'source': row[4]
                }
                articles.append(article)
            return jsonify({'articles': articles})
        else:
            return jsonify({'message': 'No articles found'}), 404
    except Exception as e:
        return jsonify({'message': 'Error retrieving articles: ' + str(e)}), 500
    finally:
        conn.close()

# Start server
if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=8080)