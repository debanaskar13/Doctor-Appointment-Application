# Re-import required modules after code execution environment reset
import pandas as pd
import random
import faker
import requests

# Initialize Faker
fake = faker.Faker()

# Constants
specialities = ['General physician', 'Gynecologist', 'Dermatologist', 'Pediatricians', 'Neurologist', 'Gastroenterologist']
degrees = ["MBBS", "MD", "DO", "DM", "MS", "MCh"]
genders = ['male', 'female']
image_base_url = "https://res.cloudinary.com/dbijetixk/image/upload/v1745093947/fkkyf9q55jwxm7cjmuax.png"

# Sample phrases for 'about' section
about_phrases = [
    "known for a compassionate approach and meticulous attention to patient care.",
    "specializes in advanced treatments and patient-centric care.",
    "brings over a decade of expertise to the field.",
    "has received numerous accolades for excellence in healthcare services.",
    "is passionate about research and continuous medical education.",
    "is highly regarded by patients for clear communication and diagnosis accuracy.",
    "is dedicated to promoting wellness through preventive measures and lifestyle advice.",
    "has led various community health initiatives with success.",
    "takes a holistic approach to medicine, combining traditional and modern practices.",
    "is an advocate for mental and physical health balance."
]

response = requests.get("https://randomuser.me/api/?gender=male&inc=name,picture&nat=us")
data = response.json()

# Get the image URL (headshot)
image_url_1 = data['results'][0]['picture']['large']

# Function to generate a doctor row
def generate_high_quality_doctor():
    gender = random.choice(genders)
    fname = fake.first_name_male() if gender == 'male' else fake.first_name_female()
    lname = fake.last_name()
    name = f"{fname} {lname}"
    username = f"{fname.lower()}.{lname.lower()}{random.randint(1, 99)}"
    password = f"Pass@{random.randint(1000,9999)}"
    email = f"{username}@example.com"
    address1 = fake.street_address()
    address2 = f"{fake.city()}, {fake.state()}"
    speciality = random.choice(specialities)
    degree = random.choice(degrees)
    experience = f"{random.randint(1, 15)} Year"
    about = f"Dr. {name}, a skilled {speciality.lower()}, {random.choice(about_phrases)}"
    fees = f"{random.randint(50, 300)}"
    phone = fake.phone_number()
    dob = fake.date_of_birth(minimum_age=30, maximum_age=60).strftime('%Y-%m-%d')
    image_url = image_url_1

    return [image_url, username, password, name, email, address1, address2, speciality, degree, experience, about, fees, phone, dob, gender]

# Columns
extended_columns = [
    "image", "username", "password", "name", "email", "address1", "address2",
    "speciality", "degree", "experience", "about", "fees", "phone", "dob", "gender"
]

# Generate 100 rows
data_extended = [generate_high_quality_doctor() for _ in range(100)]
df_extended = pd.DataFrame(data_extended, columns=extended_columns)

# Save CSV
file_path_extended = "doctors_data_extended.csv"
df_extended.to_csv(file_path_extended, index=False)

file_path_extended