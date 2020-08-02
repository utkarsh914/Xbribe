from flask import Flask,render_template,url_for,request
import pandas as pd 
import pickle
import re
from nltk.corpus import words
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer


# load the model from disk
filename = 'nlp_model.pkl'
clf = pickle.load(open(filename, 'rb'))
cv=pickle.load(open('tranform.pkl','rb'))
app = Flask(__name__)

@app.route('/')
def home():
	return render_template('home.html')

@app.route('/predict',methods=['POST'])
def predict():
	if request.method == 'POST':
		message = request.form['message']
		data = [message]
		vect = cv.transform(data).toarray()
		pred1 = clf.predict(vect)

	data = re.split(r'\W+', message)
	hindi_galis=['madarchod','behenchod','bhosra','bhosri']

	dicto={}
	lemmatizer=WordNetLemmatizer()

	for word in data:
		word=word.lower()
		if word in hindi_galis:
			return render_template('result.html',prediction =[1],msg=message)
		word=lemmatizer.lemmatize(word)
	
		if word not in words.words():
			dicto[word]=len(word)
		
		
	if not dicto:
		pred2=[0]

	else:	
		max_key = max(dicto, key=dicto.get)    
			
		if len(dicto)>5 or len(max_key)>20:
			print('SPAM')
			pred2=[1]
		else:
			print('HAM')
			pred2=[0]

	if pred1==[1] or pred2==[1]:
		my_prediction=[1]
		
	else:
		my_prediction=[0]
    
	return render_template('result.html',prediction = my_prediction,msg=message)



if __name__ == '__main__':
	app.run(debug=True)