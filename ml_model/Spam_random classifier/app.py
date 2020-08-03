from flask import Flask,render_template,url_for,request,jsonify
import pandas as pd 
import pickle
import re
from nltk.corpus import words
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer


# load the model from disk
filename = 'nlp_model.pkl'
clf = pickle.load(open(filename, 'rb'))
cv=pickle.load(open('transform.pkl','rb'))
app = Flask(__name__)

@app.route('/')
def home():
	return render_template('home.html')

@app.route('/predict',methods=['POST'])
def predict():
	if request.method == 'POST':
		message = request.form.to_dict()['message']
		data = [message]
		vect = cv.transform(data).toarray()
		pred1 = clf.predict(vect)

	if pred1==[1]:
		my_prediction=[2]
		return jsonify({ 'isspam': my_prediction[0] }), 200
		#return render_template('result.html',prediction = my_prediction,msg=message)				#return statementfor spam

	data = re.split(r'\W+', message)
	
	abusive_words=['motherfuckers','madarchod','behenchod','bhosra','bhosri','bhosdike','motherfucker','slut','whore','bullshit']	#sincerest aplogies 

	dicto={}
	pred2=[0]	#initializaton
	lemmatizer=WordNetLemmatizer()

	for word in data:
		word=word.lower()
		if word in abusive_words:
			return jsonify({ 'isspam': 2 }), 200
			#return render_template('result.html',prediction =[2],msg=message)			#return statement for abusive language
		word=lemmatizer.lemmatize(word)
	
		if word not in words.words():
			dicto[word]=len(word)
		
		
	if not dicto:
		my_prediction=[0]									# return value for ham (all okay)

	else:	
		max_key = max(dicto, key=dicto.get)    
			
		if len(dicto)>3 or len(max_key)>20:
			print('Random text or spelling mistakes')
			my_prediction=[1]								#return value for random text/spelling mistake																					
		else:
			my_prediction=[0]								#return value for ham	(all okay)
		


    
	return jsonify({ 'isspam': my_prediction[0] }), 200
	#return render_template('result.html',prediction = my_prediction,msg=message)



if __name__ == '__main__':
	app.run(debug=True)
