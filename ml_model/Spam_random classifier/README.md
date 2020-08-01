## Importing required packages


```python
import pandas as pd
import pickle
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
import pickle

from nltk.corpus import words
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
import re
```

## Loading dataset 


```python
df=pd.read_csv('spam.csv',encoding="latin-1")
```


```python
df.drop(['Unnamed: 2','Unnamed: 3','Unnamed: 4'],inplace=True,axis=1)
```


```python
df.shape
```




    (5572, 2)




```python
df.groupby('class').describe()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead tr th {
        text-align: left;
    }

    .dataframe thead tr:last-of-type th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr>
      <th></th>
      <th colspan="4" halign="left">message</th>
    </tr>
    <tr>
      <th></th>
      <th>count</th>
      <th>unique</th>
      <th>top</th>
      <th>freq</th>
    </tr>
    <tr>
      <th>class</th>
      <th></th>
      <th></th>
      <th></th>
      <th></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>ham</td>
      <td>4825</td>
      <td>4516</td>
      <td>Sorry, I'll call later</td>
      <td>30</td>
    </tr>
    <tr>
      <td>spam</td>
      <td>747</td>
      <td>653</td>
      <td>Please call our customer service representativ...</td>
      <td>4</td>
    </tr>
  </tbody>
</table>
</div>




```python
df.tail()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>class</th>
      <th>message</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>5567</td>
      <td>spam</td>
      <td>This is the 2nd time we have tried 2 contact u...</td>
    </tr>
    <tr>
      <td>5568</td>
      <td>ham</td>
      <td>Will Ì_ b going to esplanade fr home?</td>
    </tr>
    <tr>
      <td>5569</td>
      <td>ham</td>
      <td>Pity, * was in mood for that. So...any other s...</td>
    </tr>
    <tr>
      <td>5570</td>
      <td>ham</td>
      <td>The guy did some bitching but I acted like i'd...</td>
    </tr>
    <tr>
      <td>5571</td>
      <td>ham</td>
      <td>Rofl. Its true to its name</td>
    </tr>
  </tbody>
</table>
</div>




```python
#dataset = dataset.sample(frac=1).reset_index(drop=True)
```

## Data Preprocessing


```python
df['class']=df['class'].map({'ham':0,'spam':1})
```


```python
df.head()
```




<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>class</th>
      <th>message</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>0</td>
      <td>0</td>
      <td>Go until jurong point, crazy.. Available only ...</td>
    </tr>
    <tr>
      <td>1</td>
      <td>0</td>
      <td>Ok lar... Joking wif u oni...</td>
    </tr>
    <tr>
      <td>2</td>
      <td>1</td>
      <td>Free entry in 2 a wkly comp to win FA Cup fina...</td>
    </tr>
    <tr>
      <td>3</td>
      <td>0</td>
      <td>U dun say so early hor... U c already then say...</td>
    </tr>
    <tr>
      <td>4</td>
      <td>0</td>
      <td>Nah I don't think he goes to usf, he lives aro...</td>
    </tr>
  </tbody>
</table>
</div>



## Stop Words removal and Stemming 


```python
ps = PorterStemmer()
stemmed_messages = []
for i in range(0, len(df)):
    x = re.sub('[^a-zA-Z]', ' ', df['message'][i])
    x = x.lower()
    x = x.split()
    
    x = [ps.stem(word) for word in x if not word in stopwords.words('english')]
    x = ' '.join(x)
    stemmed_messages.append(x)
```




```python
cv=CountVectorizer()
X = cv.fit_transform(stemmed_messages).toarray()
y=df['class']
```


```python
pickle.dump(cv,open('transform.pkl','wb'))
```

## Splitting dataset and fitting model with optimal learning rate


```python
from sklearn.model_selection import train_test_split as TTS

X_train,X_test,y_train,y_test=TTS(X,y,test_size=0.5)
```


```python
#Multinomial Naive Bayes


for i in [0.0001,0.001,0.01,0.1,1,1.2,1.3]:
    clf=MultinomialNB(alpha=i)
    clf.fit(X_train,y_train)
    x=clf.score(X_test,y_test)
    print('For learning rate:'+str(i)+'         we get score          '+str(x))
    

clf=MultinomialNB(alpha=1)
clf.fit(X,y)    #training on whole dataset
```

    For learning rate:0.0001         we get score          0.9752333094041636
    For learning rate:0.001         we get score          0.9759511844938981
    For learning rate:0.01         we get score          0.9777458722182341
    For learning rate:0.1         we get score          0.9798994974874372
    For learning rate:1         we get score          0.9827709978463748
    For learning rate:1.2         we get score          0.9816941852117731
    For learning rate:1.3         we get score          0.9813352476669059
    




    MultinomialNB(alpha=1)




```python
pickle.dump(cv,open('nlp_model.pkl','wb'))  #Saving model for future direct use
```

## Testing


```python
data1="Hey there yo, I saw a live red-handed briber"
data2="Hey you won a voucher of Rs 10000. Send your credit card number and claim it"
```


```python
#testing
sent1=data1
sent2=data2
vect1=[sent1]
vect2=[sent2]
vect1=cv.transform(vect1).toarray()
vect2=cv.transform(vect2).toarray()
pred1=clf.predict(vect1)
pred2=clf.predict(vect2)
print(pred1)
print(pred2)

```

    [0]
    [1]
    

## The model is ready


