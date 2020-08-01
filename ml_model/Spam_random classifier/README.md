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
stemmed_messages
```




    ['go jurong point crazi avail bugi n great world la e buffet cine got amor wat',
     'ok lar joke wif u oni',
     'free entri wkli comp win fa cup final tkt st may text fa receiv entri question std txt rate c appli',
     'u dun say earli hor u c alreadi say',
     'nah think goe usf live around though',
     'freemsg hey darl week word back like fun still tb ok xxx std chg send rcv',
     'even brother like speak treat like aid patent',
     'per request mell mell oru minnaminungint nurungu vettam set callertun caller press copi friend callertun',
     'winner valu network custom select receivea prize reward claim call claim code kl valid hour',
     'mobil month u r entitl updat latest colour mobil camera free call mobil updat co free',
     'gonna home soon want talk stuff anymor tonight k cri enough today',
     'six chanc win cash pound txt csh send cost p day day tsandc appli repli hl info',
     'urgent week free membership prize jackpot txt word claim c www dbuk net lccltd pobox ldnw rw',
     'search right word thank breather promis wont take help grant fulfil promis wonder bless time',
     'date sunday',
     'xxxmobilemovieclub use credit click wap link next txt messag click http wap xxxmobilemovieclub com n qjkgighjjgcbl',
     'oh k watch',
     'eh u rememb spell name ye v naughti make v wet',
     'fine way u feel way gota b',
     'england v macedonia dont miss goal team news txt ur nation team eg england tri wale scotland txt poboxox w wq',
     'serious spell name',
     'go tri month ha ha joke',
     'pay first lar da stock comin',
     'aft finish lunch go str lor ard smth lor u finish ur lunch alreadi',
     'ffffffffff alright way meet sooner',
     'forc eat slice realli hungri tho suck mark get worri know sick turn pizza lol',
     'lol alway convinc',
     'catch bu fri egg make tea eat mom left dinner feel love',
     'back amp pack car let know room',
     'ahhh work vagu rememb feel like lol',
     'wait still clear sure sarcast x want live us',
     'yeah got v apologet n fallen actin like spoilt child got caught till go badli cheer',
     'k tell anyth',
     'fear faint housework quick cuppa',
     'thank subscript rington uk mobil charg month pleas confirm repli ye repli charg',
     'yup ok go home look time msg xuhui go learn nd may lesson',
     'oop let know roommat done',
     'see letter b car',
     'anyth lor u decid',
     'hello saturday go text see decid anyth tomo tri invit anyth',
     'pl go ahead watt want sure great weekend abiola',
     'forget tell want need crave love sweet arabian steed mmmmmm yummi',
     'rodger burn msg tri call repli sm free nokia mobil free camcord pleas call deliveri tomorrow',
     'see',
     'great hope like man well endow lt gt inch',
     'call messag miss call',
     'get hep b immunis nigeria',
     'fair enough anyth go',
     'yeah hope tyler could mayb ask around bit',
     'u know stubborn even want go hospit kept tell mark weak sucker hospit weak sucker',
     'think first time saw class',
     'gram usual run like lt gt half eighth smarter though get almost whole second gram lt gt',
     'k fyi x ride earli tomorrow morn crash place tonight',
     'wow never realiz embarass accomod thought like sinc best could alway seem happi cave sorri give sorri offer sorri room embarass',
     'sm ac sptv new jersey devil detroit red wing play ice hockey correct incorrect end repli end sptv',
     'know mallika sherawat yesterday find lt url gt',
     'congrat year special cinema pass call c suprman v matrix starwar etc free bx ip pm dont miss',
     'sorri call later meet',
     'tell reach',
     'ye gauti sehwag odi seri',
     'gonna pick burger way home even move pain kill',
     'ha ha ha good joke girl situat seeker',
     'part check iq',
     'sorri roommat took forev ok come',
     'ok lar doubl check wif da hair dresser alreadi said wun cut v short said cut look nice',
     'valu custom pleas advis follow recent review mob award bonu prize call',
     'today song dedic day song u dedic send ur valuabl frnd first rpli',
     'urgent ur award complimentari trip eurodisinc trav aco entri claim txt di morefrmmob shracomorsglsuplt ls aj',
     'hear new divorc barbi come ken stuff',
     'plane give month end',
     'wah lucki man save money hee',
     'finish class',
     'hi babe im home wanna someth xx',
     'k k perform',
     'u call',
     'wait machan call free',
     'that cool gentleman treat digniti respect',
     'like peopl much shi pa',
     'oper lt gt',
     'still look job much ta earn',
     'sorri call later',
     'k call ah',
     'ok way home hi hi',
     'place man',
     'yup next stop',
     'call later network urgnt sm',
     'real u get yo need ticket one jacket done alreadi use multi',
     'ye start send request make pain came back back bed doubl coin factori gotta cash nitro',
     'realli still tonight babe',
     'ela kano il download come wen ur free',
     'yeah stand close tho catch someth',
     'sorri pain ok meet anoth night spent late afternoon casualti mean done stuff moro includ time sheet sorri',
     'smile pleasur smile pain smile troubl pour like rain smile sum hurt u smile becoz someon still love see u smile',
     'pleas call custom servic repres pm guarante cash prize',
     'havent plan buy later check alreadi lido got show e afternoon u finish work alreadi',
     'free rington wait collect simpli text password mix verifi get usher britney fml',
     'watch telugu movi wat abt u',
     'see finish load loan pay',
     'hi wk ok hol ye bit run forgot hairdress appoint four need get home n shower beforehand caus prob u ham',
     'pleas text anymor noth els say',
     'okay name ur price long legal wen pick u ave x am xx',
     'still look car buy gone drive test yet',
     'per request mell mell oru minnaminungint nurungu vettam set callertun caller press copi friend callertun',
     'wow right mean guess gave boston men chang search locat nyc someth chang cuz signin page still say boston',
     'umma life vava umma love lot dear',
     'thank lot wish birthday thank make birthday truli memor',
     'aight hit get cash',
     'would ip address test consid comput minecraft server',
     'know grumpi old peopl mom like better lie alway one play joke',
     'dont worri guess busi',
     'plural noun research',
     'go dinner msg',
     'ok wif co like tri new thing scare u dun like mah co u said loud',
     'gent tri contact last weekend draw show prize guarante call claim code k valid hr ppm',
     'wa ur openin sentenc formal anyway fine juz tt eatin much n puttin weight haha anythin special happen',
     'enter cabin pa said happi b day boss felt special askd lunch lunch invit apart went',
     'winner u special select receiv holiday flight inc speak live oper claim p min',
     'goodo ye must speak friday egg potato ratio tortilla need',
     'hmm uncl inform pay school directli pl buy food',
     'privat account statement show unredeem bonu point claim call identifi code expir',
     'urgent mobil award bonu caller prize final tri contact u call landlin box wr c ppm',
     'new address appl pair malarki',
     'today voda number end select receiv award match pleas call quot claim code standard rate app',
     'go sao mu today done',
     'predict wat time finish buy',
     'good stuff',
     'know yetund sent money yet sent text bother send dont involv anyth impos anyth first place apologis',
     'room',
     'hey girl r u hope u r well del r bak long time c give call sum time lucyxx',
     'k k much cost',
     'home',
     'dear call tmorrow pl accomod',
     'first answer question',
     'sunshin quiz wkli q win top soni dvd player u know countri algarv txt ansr sp tyron',
     'want get laid tonight want real dog locat sent direct ur mob join uk largest dog network bt txting gravel nt ec p msg p',
     'haf msn yiju hotmail com',
     'call meet',
     'check room befor activ',
     'rcv msg chat svc free hardcor servic text go u get noth u must age verifi yr network tri',
     'got c lazi type forgot lect saw pouch like v nice',
     'k text way',
     'sir wait mail',
     'swt thought nver get tire littl thing lovabl person coz somtim littl thing occupi biggest part heart gud ni',
     'know pl open back',
     'ye see ya dot',
     'what staff name take class us',
     'freemsg repli text randi sexi femal live local luv hear u netcollex ltd p per msg repli stop end',
     'ummma call check life begin qatar pl pray hard',
     'k delet contact',
     'sindu got job birla soft',
     'wine flow never',
     'yup thk cine better co need go plaza mah',
     'ok ur typic repli',
     'per request mell mell oru minnaminungint nurungu vettam set callertun caller press copi friend callertun',
     'everywher dirt floor window even shirt sometim open mouth come flow dream world without half chore time joy lot tv show see guess like thing must exist like rain hail mist time done becom one',
     'aaooooright work',
     'leav hous',
     'hello love get interview today happi good boy think miss',
     'custom servic annonc new year deliveri wait pleas call arrang deliveri',
     'winner u special select receiv cash holiday flight inc speak live oper claim',
     'keep safe need miss alreadi envi everyon see real life',
     'new car hous parent new job hand',
     'love excit day spend make happi',
     'pl stop bootydeli f invit friend repli ye see www sm ac u bootydeli stop send stop frnd',
     'bangbab ur order way u receiv servic msg download ur content u goto wap bangb tv ur mobil internet servic menu',
     'place ur point e cultur modul alreadi',
     'urgent tri contact last weekend draw show prize guarante call claim code valid hr',
     'hi frnd best way avoid missunderstd wit belov one',
     'great escap fanci bridg need lager see tomo',
     'ye complet form clark also utter wast',
     'sir need axi bank account bank address',
     'hmmm thk sure got time hop ard ya go free abt muz call u discuss liao',
     'time come later',
     'bloodi hell cant believ forgot surnam mr ill give u clue spanish begin',
     'well gonna finish bath good fine night',
     'let know got money carlo make call',
     'u still go mall',
     'turn friend stay whole show back til lt gt feel free go ahead smoke lt gt worth',
     'text doesnt repli let know log',
     'hi spoke maneesha v like know satisfi experi repli toll free ye',
     'lift hope offer money need especi end month approach hurt studi anyway gr weekend',
     'lol u trust',
     'ok gentleman treat digniti respect',
     'guy close',
     'go noth great bye',
     'hello handsom find job lazi work toward get back net mummi boytoy miss',
     'haha awesom minut',
     'pleas call custom servic repres freephon pm guarante cash prize',
     'got xma radio time get',
     'ju reach home go bath first si use net tell u finish k',
     'uniqu enough find th august www areyouuniqu co uk',
     'sorri join leagu peopl dont keep touch mean great deal friend time even great person cost great week',
     'hi final complet cours',
     'stop howev suggest stay someon abl give or everi stool',
     'hope settl new school year wishin gr day',
     'gud mrng dear hav nice day',
     'u got person stori',
     'hamster dead hey tmr meet pm orchard mrt',
     'hi kate even hope see tomorrow bit bloodi babyjontet txt back u xxx',
     'found enc lt gt',
     'sent lt gt buck',
     'hello darlin ive finish colleg txt u finish u love kate xxx',
     'account refil success inr lt decim gt keralacircl prepaid account balanc rs lt decim gt transact id kr lt gt',
     'goodmorn sleep ga',
     'u call alter ok',
     'say like dat dun buy ericsson oso cannot oredi lar',
     'enter cabin pa said happi b day boss felt special askd lunch lunch invit apart went',
     'aight yo dat straight dogg',
     'pleas give us connect today lt decim gt refund bill',
     'shoot big load get readi',
     'bruv hope great break reward semest',
     'home alway chat',
     'k k good studi well',
     'yup noe leh',
     'sound great home',
     'final match head toward draw predict',
     'tire slept well past night',
     'easi ah sen got select mean good',
     'take exam march',
     'yeah think use gt atm regist sure anyway help let know sure readi',
     'ok prob take ur time',
     'os call ubandu run without instal hard disk use os copi import file system give repair shop',
     'sorri call later',
     'u say leh cours noth happen lar say v romant ju bit lor thk e nite sceneri nice leh',
     'new mobil must go txt nokia collect today www tc biz optout gbp mtmsg',
     'would realli appreci call need someon talk',
     'u meet ur dream partner soon ur career flyng start find free txt horo follow ur star sign e g horo ari',
     'hey compani elama po mudyadhu',
     'life strict teacher bcoz teacher teach lesson amp conduct exam life first conduct exam amp teach lesson happi morn',
     'dear good morn',
     'get gandhipuram walk cross cut road right side lt gt street road turn first right',
     'dear go rubber place',
     'sorri batteri die yeah',
     'ye tv alway avail work place',
     'text meet someon sexi today u find date even flirt u join p repli name age eg sam msg recd thirtyeight penc',
     'print oh lt gt come upstair',
     'ill littl closer like bu stop street',
     'wil reach',
     'new theori argument win situat lose person dont argu ur friend kick amp say alway correct',
     'u secret admir look make contact u find r reveal think ur special call',
     'tomarrow final hear laptop case cant',
     'pleassssssseeeee tel v avent done sportsx',
     'okay shine meant sign sound better',
     'although told u dat baig face watch realli like e watch u gave co fr u thanx everyth dat u done today touch',
     'u rememb old commerci',
     'late said websit dont slipper',
     'ask call ok',
     'kalli wont bat nd inning',
     'didnt work oh ok goodnight fix readi time wake dearli miss good night sleep',
     'congratul ur award cd voucher gift guarante free entri wkli draw txt music tnc www ldew com win ppmx age',
     'ranjith cal drpd deeraj deepak min hold',
     'wen ur lovabl bcum angri wid u dnt take serious coz angri childish n true way show deep affect care n luv kettoda manda nice day da',
     '',
     'up day also ship compani take wk way usp take week get lag may bribe nipost get stuff',
     'back lemm know readi',
     'necessarili expect done get back though headin',
     'mmm yummi babe nice jolt suzi',
     'lover need',
     'tri contact repli offer video handset anytim network min unlimit text camcord repli call',
     'park next mini come today think',
     'yup',
     'anyway go shop co si done yet dun disturb u liao',
     'luton ring ur around h',
     'hey realli horni want chat see nake text hot text charg pm unsubscrib text stop',
     'dint come us',
     'wana plan trip sometm',
     'sure yet still tri get hold',
     'ur rington servic chang free credit go club mobil com choos content stop txt club stop p wk club po box mk wt',
     'evo download flash jealou',
     'rington club get uk singl chart mobil week choos top qualiti rington messag free charg',
     'come mu sort narcot situat',
     'night end anoth day morn come special way may smile like sunni ray leav worri blue blue bay',
     'hmv bonu special pound genuin hmv voucher answer easi question play send hmv info www percent real com',
     'usf guess might well take car',
     'object bf come',
     'thanx',
     'tell rob mack gf theater',
     'awesom see bit',
     'sent type food like',
     'done hand celebr full swing yet',
     'got call tool',
     'wen u miss someon',
     'ok ask money far',
     'oki',
     'yeah think usual guy still pass last night get ahold anybodi let know throw',
     'k might come tonight class let earli',
     'ok',
     'hi babi im cruisin girl friend r u give call hour home that alright fone fone love jenni xxx',
     'life mean lot love life love peopl life world call friend call world ge',
     'dear shall mail tonit busi street shall updat tonit thing look ok varunnathu edukkukaye raksha ollu good one real sens',
     'hey told name gautham ah',
     'haf u found feel stupid da v cam work',
     'oop got bit',
     'much buzi',
     'accident delet messag resend pleas',
     'mobil custom may claim free camera phone upgrad pay go sim card loyalti call offer end thfeb c appli',
     'unless situat go gurl would appropri',
     'hurt teas make cri end life die plz keep one rose grave say stupid miss u nice day bslvyl',
     'cant pick phone right pl send messag',
     'need coffe run tomo believ time week alreadi',
     'awesom rememb last time got somebodi high first time diesel v',
     'shit realli shock scari cant imagin second def night u think somewher could crash night save taxi',
     'oh way food fridg want go meal tonight',
     'womdarful actor',
     'sm ac blind date u rodd aberdeen unit kingdom check http img sm ac w icmb cktz r blind date send hide',
     'yup remb think book',
     'jo ask u wana meet',
     'lol ye friendship hang thread caus u buy stuff',
     'themob check newest select content game tone gossip babe sport keep mobil fit funki text wap',
     'garag key bookshelf',
     'today accept day u accept brother sister lover dear best clo lvblefrnd jstfrnd cutefrnd lifpartnr belovd swtheart bstfrnd rpli mean enemi',
     'think ur smart win week weekli quiz text play cs winnersclub po box uz gbp week',
     'say give call friend got money definit buy end week',
     'hi way u day normal way real ur uniqu hope know u rest mylif hope u find wot lost',
     'made day great day',
     'k k advanc happi pongal',
     'hmmm guess go kb n power yoga haha dunno tahan power yoga anot thk got lo oso forgot liao',
     'realli dude friend afraid',
     'decemb mobil mth entitl updat latest colour camera mobil free call mobil updat co free',
     'coffe cake guess',
     'merri christma babe love ya kiss',
     'hey dont go watch x men lunch haha',
     'cud u tell ppl im gona b bit l co buse hav gon past co full im still waitin pete x',
     'would great guild could meet bristol road somewher get touch weekend plan take flight good week',
     'problem',
     'call messag miss call',
     'hi da today class',
     'say good sign well know track record read women',
     'cool text park',
     'read text sent meant joke read light',
     'k k apo k good movi',
     'mayb could get book tomo return immedi someth',
     'call germani penc per minut call fix line via access number prepay direct access',
     'chanc might evapor soon violat privaci steal phone number employ paperwork cool pleas contact report supervisor',
     'valentin day special win quiz take partner trip lifetim send go p msg rcvd custcar',
     'ta daaaaa home babe still',
     'cool come havent wine dine',
     'sleep surf',
     'sorri call later',
     'u call right call hand phone',
     'ok great thanx lot',
     'take post come must text happi read one wiv hello carolin end favourit bless',
     'u hide stranger',
     'interest like',
     'sister clear two round birla soft yesterday',
     'gudnit tc practic go',
     'di yiju ju saw ur mail case huim havent sent u num di num',
     'one small prestig problem',
     'fanci shag interest sextextuk com txt xxuk suzi txt cost per msg tnc websit x',
     'check realli miss see jeremiah great month',
     'nah help never iphon',
     'car hour half go apeshit',
     'today sorri day ever angri ever misbehav hurt plz plz slap urself bcoz ur fault basic good',
     'yo guy ever figur much need alcohol jay tri figur much safe spend weed',
     'lt gt ish minut minut ago wtf',
     'thank call forgot say happi onam sirji fine rememb met insur person meet qatar insha allah rakhesh ex tata aig join tissco tayseer',
     'congratul ur award cd voucher gift guarante free entri wkli draw txt music tnc www ldew com win ppmx age',
     'ur cash balanc current pound maxim ur cash send cash p msg cc hg suit land row w j hl',
     'actor work work even sleep late sinc unemploy moment alway sleep late unemploy everi day saturday',
     'hello got st andrew boy long way cold keep post',
     'ha ha cool cool chikku chikku db',
     'oh ok prob',
     'check audrey statu right',
     'busi tri finish new year look forward final meet',
     'good afternoon sunshin dawn day refresh happi aliv breath air smile think love alway',
     'well know z take care worri',
     'updat xma offer latest motorola sonyericsson nokia free bluetooth doubl min txt orang call mobileupd call optout f q',
     'discount code rp stop messag repli stop www regalportfolio co uk custom servic',
     'wat uniform get',
     'cool text readi',
     'hello boytoy geeee miss alreadi woke wish bed cuddl love',
     'spoil bed well',
     'go bath msg next lt gt min',
     'cant keep talk peopl sure pay agre price pl tell want realli buy much will pay',
     'thank rington order refer charg gbp per week unsubscrib anytim call custom servic',
     'say happen',
     'could seen recognis face',
     'well lot thing happen lindsay new year sigh bar ptbo blue heron someth go',
     'keep payasam rinu bring',
     'taught ranjith sir call sm like becau he verifi project prabu told today pa dont mistak',
     'guess worri must know way bodi repair quit sure worri take slow first test guid ovul relax noth said reason worri keep followin',
     'yeah sure give coupl minut track wallet',
     'hey leav big deal take care',
     'hey late ah meet',
     'doubl min txt month free bluetooth orang avail soni nokia motorola phone call mobileupd call optout n dx',
     'took mr owl lick',
     'custom place call',
     'mm time dont like fun',
     'mth half price orang line rental latest camera phone free phone mth call mobilesdirect free updat stoptxt',
     'yup lunch buffet u eat alreadi',
     'huh late fr dinner',
     'hey sat go intro pilat kickbox',
     'morn ok',
     'ye think offic lap room think that last day didnt shut',
     'pick bout ish time go',
     'perform award calcul everi two month current one month period',
     'actual sleep still might u call back text gr rock si send u text wen wake',
     'alway put busi put pictur ass facebook one open peopl ever met would think pictur room would hurt make feel violat',
     'good even sir al salam wahleykkum share happi news grace god got offer tayseer tissco join hope fine inshah allah meet sometim rakhesh visitor india',
     'hmmm k want chang field quickli da wanna get system administr network administr',
     'free rington text first poli text get true tone help st free tone x pw e nd txt stop',
     'dear chechi talk',
     'hair cream ship',
     'none happen til get though',
     'yep great loxahatche xma tree burn lt gt start hour',
     'haha get use drive usf man know lot stoner',
     'well slightli disastr class pm fav darl hope day ok coffe wld good stay late tomorrow time place alway',
     'hello good week fanci drink someth later',
     'headin toward busetop',
     'messag text miss sender name miss number miss sent date miss miss u lot that everyth miss sent via fullonsm com',
     'come room point iron plan weekend',
     'co want thing',
     'oki go yan jiu skip ard oso go cine den go mrt one blah blah blah',
     'bring home wendi',
     'date servic cal l box sk ch',
     'whatsup dont u want sleep',
     'alright new goal',
     'free entri weekli competit text word win c www txttowin co uk',
     'alright head minut text meet',
     'send logo ur lover name join heart txt love name name mobno eg love adam eve yahoo pobox w wq txtno ad p',
     'ye last week take live call',
     'someon contact date servic enter phone fanci find call landlin pobox n tf p',
     'siva hostel aha',
     'urgent mobil number award prize guarante call land line claim valid hr',
     'send ur friend receiv someth ur voic speak express childish naughti sentiment rowdi ful attitud romant shi attract funni lt gt irrit lt gt lovabl repli',
     'ok ok guess',
     'aathi dear',
     'pain urin thing els',
     'esplanad mind give lift co got car today',
     'wnt buy bmw car urgent vri urgent hv shortag lt gt lac sourc arng di amt lt gt lac that prob',
     'home watch tv lor',
     'usual take fifteen fuck minut respond ye question',
     'congrat nokia video camera phone call call cost ppm ave call min vari mobil close post bcm ldn wc n xx',
     'book ticket pongal',
     'avail like right around hillsborough amp lt gt th',
     'messag sent askin lt gt dollar shoul pay lt gt lt gt',
     'ask g iouri told stori like ten time alreadi',
     'long applebe fuck take',
     'hi hope u get txt journey hasnt gd min late think',
     'like love arrang',
     'ye realli great bhaji told kalli best cricket sachin world tough get',
     'suppos wake gt',
     'oic saw tot din c found group liao',
     'sorri call later',
     'hey hey wereth monkeespeopl say monkeyaround howdi gorgeou',
     'sorri batteri die come get gram place',
     'well done blimey exercis yeah kinda rememb wot hmm',
     'wont get concentr dear know mind everyth',
     'lol made plan new year',
     'min later k',
     'hank lotsli',
     'thank hope good day today',
     'k k detail want transfer acc enough',
     'ok tell stay yeah tough optimist thing improv month',
     'loan purpos homeown tenant welcom previous refus still help call free text back help',
     'si si think ill go make oreo truffl',
     'look ami ure beauti intellig woman like u lot know u like like worri',
     'hope result consist intellig kind start ask practicum link keep ear open best ttyl',
     'call cost guess isnt bad miss ya need ya want ya love ya',
     'go thru differ feel waver decis cope individu time heal everyth believ',
     'u go phone gonna die stay',
     'great never better day give even reason thank god',
     'upgrdcentr orang custom may claim free camera phone upgrad loyalti call offer end th juli c appli opt avail',
     'sorri call later ok bye',
     'ok way railway',
     'great princess love give receiv oral doggi style fave posit enjoy make love lt gt time per night',
     'put stuff road keep get slipperi',
     'go ride bike',
     'yup need ju wait e rain stop',
     'mani compani tell languag',
     'okmail dear dave final notic collect tenerif holiday cash award call landlin tc sae box cw wx ppm',
     'long sinc scream princess',
     'noth meant money enter account bank remov flat rate someon transfer lt gt account lt gt dollar got remov bank differ charg also differ sure trust ja person send account detail co',
     'want get laid tonight want real dog locat sent direct ur mob join uk largest dog network txting moan nyt ec p msg p',
     'nice line said broken heart plz cum time infront wise trust u good',
     'ok gonna head usf like fifteen minut',
     'love aathi love u lot',
     'tension ah machi problem',
     'k pick anoth th done',
     'guy get back g said think stay mcr',
     'almost see u sec',
     'yo carlo friend alreadi ask work weekend',
     'watch tv lor',
     'thank babi cant wait tast real thing',
     'chang fb jaykwon thuglyf falconerf',
     'win realli side long time',
     'free messag activ free text messag repli messag word free term condit visit www com',
     'dear reach railway happen',
     'depend qualiti want type sent boy fade glori want ralph mayb',
     'think fix send test messag',
     'sorri man account dri would want could trade back half could buy shit credit card',
     'congrat year special cinema pass call c suprman v matrix starwar etc free bx ip pm dont miss',
     'sorri meet call later',
     'class lt gt reunion',
     'free call',
     'got meh',
     'nope think go monday sorri repli late',
     'told accentur confirm true',
     'kate jackson rec center ish right',
     'dear reach room',
     'fight world easi u either win lose bt fightng close u dificult u lose u lose u win u still lose',
     'come',
     'check nuerologist',
     'lolnic went fish water',
     'congratul week competit draw u prize claim call b cs stop sm ppm',
     'wait e car dat bore wat co wait outsid got noth home stuff watch tv wat',
     'mayb westshor hyde park villag place near hous',
     'know anthoni bring money school fee pay rent stuff like that need help friend need',
     'signific',
     'opinion jada kusruthi lovabl silent spl charact matur stylish simpl pl repli',
     'latest g still scroung ammo want give new ak tri',
     'prabha soryda reali frm heart sori',
     'lol ok forgiven',
     'jst chang tat',
     'guarante latest nokia phone gb ipod mp player prize txt word collect ibhltd ldnw h p mtmsgrcvd',
     'competit',
     'boltblu tone p repli poli mono eg poli cha cha slide yeah slow jamz toxic come stop tone txt',
     'credit top http www bubbletext com renew pin tgxxrz',
     'way transport less problemat sat night way u want ask n join bday feel free need know definit no book fri',
     'usual person unconsci children adult may behav abnorm call',
     'ebay might less elsewher',
     'shall come get pickl',
     'gonna go get taco',
     'rude campu',
     'urgent mobil award bonu caller prize nd attempt contact call box qu',
     'hi b ard christma enjoy n merri x ma',
     'today offer claim ur worth discount voucher text ye savamob member offer mobil cs sub unsub repli x',
     'ye pretti ladi like singl',
     'reciev tone within next hr term condit pleas see channel u teletext pg',
     'jay say doubl faggot',
     'privat account statement show un redeem point call identifi code expir',
     'today sunday sunday holiday work',
     'gudnit tc practic go',
     'late',
     'call hope l r malaria know miss guy miss bani big pl give love especi great day',
     'good afternoon love goe day hope mayb got lead job think boytoy send passion kiss across sea',
     'probabl gonna see later tonight lt',
     'mayb fat finger press button know',
     'ummmmmaah mani mani happi return day dear sweet heart happi birthday dear',
     'tirupur da start offic call',
     'www applausestor com monthlysubscript p msg max month csc web age stop txt stop',
     'famou quot develop abil listen anyth uncondit without lose temper self confid mean marri',
     'go colleg pa els ill come self pa',
     'oclock mine bash flat plan',
     'girl stay bed girl need recoveri time id rather pass fun coop bed',
     'special',
     'know need get hotel got invit apologis cali sweet come english bloke weddin',
     'sorri took long omw',
     'wait lt gt min',
     'ok give minut think see btw alibi cut hair whole time',
     'imagin final get sink bath put pace mayb even eat left also imagin feel cage cock surround bath water remind alway own enjoy cuck',
     'hurri weed defici like three day',
     'sure get acknowledg astoundingli tactless gener faggi demand blood oath fo',
     'ok everi night take warm bath drink cup milk see work magic still need loos weight know',
     'look fri pan case cheap book perhap silli fri pan like book',
     'well uv caus mutat sunscreen like essenti theseday',
     'lunch onlin',
     'know friend alreadi told',
     'hi princess thank pic pretti',
     'aiyo u alway c ex one dunno abt mei repli first time u repli fast lucki workin huh got bao ur sugardad ah gee',
     'hi msg offic',
     'thanx e browni v nice',
     'geeeee love much bare stand',
     'gent tri contact last weekend draw show prize guarante call claim code k valid hr ppm',
     'fuck babe miss alreadi know let send money toward net need want crave',
     'ill call u mrw ninish address icki american freek wont stop callin bad jen k eh',
     'oooh bed ridden ey think',
     'anyway go gym whatev love smile hope ok good day babe miss much alreadi',
     'love daddi make scream pleasur go slap ass dick',
     'wot u wanna missi',
     'yar lor wait mum finish sch lunch lor whole morn stay home clean room room quit clean hee',
     'know lab goggl went',
     'open door',
     'wait call',
     'nope wait sch daddi',
     'cash prize claim call',
     'tire argu week week want',
     'wait sch finish ard',
     'mobil number claim call us back ring claim hot line',
     'arngd marriag u r walkin unfortuntli snake bite u bt love marriag danc frnt snake amp sayin bite bite',
     'huh earli dinner outsid izzit',
     'ok anyway need chang said',
     'tri contact repli offer min textand new video phone call repli free deliveri tomorrow',
     'ex wife abl kid want kid one day',
     'scotland hope show jjc tendenc take care live dream',
     'tell u headach want use hour sick time',
     'dun thk quit yet hmmm go jazz yogasana oso go meet em lesson den',
     'pete pleas ring meiv hardli gotani credit',
     'ya srsli better yi tho',
     'meet call later',
     'ur chanc win wkli shop spree txt shop c www txt shop com custcar x p wk',
     'special select receiv pound award call line close cost ppm cs appli ag promo',
     'privat account statement show un redeem point call identifi code expir',
     'still grand prix',
     'met stranger choos friend long world stand friendship never end let friend forev gud nitz',
     'great',
     'gud mrng dear nice day',
     'import custom servic announc call freephon',
     'exhaust train morn much wine pie sleep well',
     'go buy mum present ar',
     'mind blastin tsunami occur rajnik stop swim indian ocean',
     'u send home first ok lor readi yet',
     'speak cash yet',
     'happi come noon',
     'meet lunch la',
     'take care n get well soon',
     'xclusiv clubsaisai morow soire special zouk nichol pari free rose ladi info',
     'meant say cant wait see u get bore bridgwat banter',
     'neva mind ok',
     'fine imma get drink somethin want come find',
     'day kick euro u kept date latest news result daili remov send get txt stop',
     'valentin game send di msg ur friend answer r someon realli love u que colour suit best rpli',
     'mani depend',
     'thanx today cer nice catch ave find time often oh well take care c u soon c',
     'call said choos futur',
     'happi valentin day know earli hundr handsom beauti wish thought finish aunti uncl st',
     'like v shock leh co tell shuhui like tell leona also like dat almost know liao got ask abt ur reaction lor',
     'famili happi',
     'come n pick come immedi aft ur lesson',
     'let snow let snow kind weather bring ppl togeth friendship grow',
     'dear got lt gt dollar hi hi',
     'good word word may leav u dismay mani time',
     'make sure alex know birthday fifteen minut far concern',
     'sorri got thing may pub later',
     'nah straight bring bud drink someth actual littl use straight cash',
     'haha good hear offici paid market th',
     'mani lick take get center tootsi pop',
     'yup thk r e teacher said make face look longer darren ask cut short',
     'new textbuddi chat horni guy ur area p free receiv search postcod gaytextbuddi com txt one name',
     'today vodafon number end select receiv award number match call receiv award',
     'pleas dont say like hi hi hi',
     'thank u',
     'oh forward messag thought send',
     'got seventeen pound seven hundr ml hope ok',
     'dear voucher holder claim week offer pc go http www e tlp co uk expressoff ts cs appli stop text txt stop',
     'n funni',
     'sweetheart hope kind day one load reason smile biola',
     'login dat time dad fetch home',
     'shower babi',
     'askd u question hour answer',
     'well imma definit need restock thanksgiv let know',
     'said kiss kiss sound effect gorgeou man kind person need smile brighten day',
     'probabl gonna swing wee bit',
     'ya nice readi thursday',
     'allo brave buse taken train triumph mean b ham jolli good rest week',
     'watch cartoon listen music amp eve go templ amp church u',
     'mind ask happen dont say uncomfort',
     'privat account statement show un redeem point call identifi code expir',
     'prob send email',
     'cash prize claim call c rstm sw ss ppm',
     'that cool sometim slow gentl sonetim rough hard',
     'gonna say sorri would normal start panic time sorri see tuesday',
     'wait know wesley town bet hella drug',
     'fine miss much',
     'u got person stori',
     'tell drug dealer get impati',
     'sun cant come earth send luv ray cloud cant come river send luv rain cant come meet u send care msg u gud evng',
     'place man',
     'doesnt make sens take unless free need know wikipedia com',
     'premium phone servic call',
     'sea lay rock rock envelop envelop paper paper word',
     'mum repent',
     'sorri go home first daddi come fetch later',
     'leav de start prepar next',
     'ye babi studi posit kama sutra',
     'en chikku nang bakra msg kalstiya tea coffe',
     'carlo minut still need buy',
     'pay lt decim gt lakh',
     'good even ttyl',
     'u receiv msg',
     'ho ho big belli laugh see ya tomo',
     'sm ac sun post hello seem cool',
     'get ur st rington free repli msg tone gr top tone phone everi week per wk opt send stop',
     'ditto worri say anyth anymor like said last night whatev want peac',
     'got lt gt way could pick',
     'dont knw pa drink milk',
     'mayb say hi find got card great escap wetherspoon',
     'piggi r u awak bet u still sleep go lunch',
     'caus freaki lol',
     'miss call caus yell scrappi miss u wait u come home lone today',
     'hex place talk explain',
     'log wat sdryb',
     'xy go e lunch',
     'hi sue year old work lapdanc love sex text live bedroom text sue textoper g da ppmsg',
     'want ask wait finish lect co lect finish hour anyway',
     'finish work yet',
     'everi king cri babi everi great build map imprtant u r today u wil reach tomorw gud ni',
     'dear cherthala case u r come cochin pl call bfore u start shall also reach accordingli tell day u r come tmorow engag an holiday',
     'thank love torch bold',
     'forward pleas call immedi urgent messag wait',
     'farm open',
     'sorri troubl u buy dad big small sat n sun thanx',
     'sister law hope great month say hey abiola',
     'purchas stuff today mail po box number',
     'ah poop look like ill prob send laptop get fix cuz gpu problem',
     'good good job like entrepreneur',
     'aight close still around alex place',
     'meet corpor st outsid gap see mind work',
     'mum ask buy food home',
     'k u also dont msg repli msg',
     'much r will pay',
     'sorri call later',
     'import prevent dehydr give enough fluid',
     'that bit weird even suppos happen good idea sure pub',
     'true dear sat pray even felt sm time',
     'think get away trek long famili town sorri',
     'wanna gym harri',
     'quit late lar ard anyway wun b drivin',
     'review keep fantast nokia n gage game deck club nokia go www cnupdat com newslett unsubscrib alert repli word',
     'mth half price orang line rental latest camera phone free phone mth call mobilesdirect free updat stoptxt cs',
     'height confid aeronaut professor wer calld amp wer askd sit aeroplan aftr sat wer told dat plane ws made student dey hurri plane bt didnt move said made student',
     'seem like weird time night g want come smoke day shitstorm attribut alway come make everyon smoke',
     'pm cost p',
     'save stress person dorm account send account detail money sent',
     'also know lunch menu da know',
     'stuff sell tell',
     'urgent nd attempt contact u u call b csbcm wc n xx callcost ppm mobilesvari max',
     'book lesson msg call work sth go get spec membership px',
     'guarante cash prize claim yr prize call custom servic repres pm',
     'macha dont feel upset assum mindset believ one even wonder plan us let life begin call anytim',
     'oh send address',
     'fine anytim best',
     'wondar full flim',
     'ya even cooki jelli',
     'world run still mayb feel admit mad correct let call life keep run world may u r also run let run',
     'got look scrumptiou daddi want eat night long',
     'co lar ba dao ok pm lor u never ask go ah said u would ask fri said u ask today',
     'alright omw gotta chang order half th',
     'exactli anyway far jide studi visit',
     'dunno u ask',
     'email alertfrom jeri stewarts kbsubject low cost prescripiton drvgsto listen email call',
     'spring come earli yay',
     'lol feel bad use money take steak dinner',
     'even u dont get troubl convinc tel twice tel neglect msg dont c read dont repli',
     'leav qatar tonit search opportun went fast pl add ur prayer dear rakhesh',
     'one talk',
     'thank look realli appreci',
     'hi custom loyalti offer new nokia mobil txtauction txt word start get ctxt tc p mtmsg',
     'wish',
     'haha mayb u rite u know well da feel like someon gd lor u faster go find one gal group attach liao',
     'ye glad made',
     'well littl time thing good time ahead',
     'got room soon put clock back til shout everyon get realis wahay anoth hour bed',
     'ok may free gym',
     'men like shorter ladi gaze eye',
     'dunno ju say go lido time',
     'promis take good care princess run pleas send pic get chanc ttyl',
     'u subscrib best mobil content servic uk per day send stop helplin',
     'reason spoken year anyway great week best exam',
     'monday next week give full gist',
     'realiz year thousand old ladi run around tattoo',
     'import custom servic announc premier',
     'dont gimm lip caveboy',
     'get librari',
     'reali sorri recognis number confus r u pleas',
     'didnt holla',
     'cant think anyon spare room top head',
     'faith make thing possibl hope make thing work love make thing beauti may three christma merri christma',
     'u made appoint',
     'call carlo phone vibrat act might hear text',
     'romant pari night flight book next year call ts cs appli',
     'grandma oh dear u still ill felt shit morn think hungov anoth night leav sat',
     'urgent ur guarante award still unclaim call closingd claimcod pmmorefrommobil bremov mobypobox ls yf',
     'noth ju tot u would ask co u ba gua went mt faber yest yest ju went alreadi mah today go ju call lor',
     'wish famili merri x ma happi new year advanc',
     'ur award citi break could win summer shop spree everi wk txt store skilgm tsc winawk age perwksub',
     'nt goin got somethin unless meetin dinner lor haha wonder go ti time',
     'sorri call later',
     'cant pick phone right pl send messag',
     'lol know dramat school alreadi close tomorrow appar drive inch snow suppos get',
     'get anywher damn job hunt',
     'lol u drunkard hair moment yeah still tonight wat plan',
     'idc get weasel way shit twice row',
     'wil lt gt minut got space',
     'sleep surf',
     'thank pick trash',
     'go tell friend sure want live smoke much spend hour beg come smoke',
     'hi kate love see tonight ill phone tomorrow got sing guy gave card xxx',
     'happi new year dear brother realli miss got number decid send text wish happi abiola',
     'mean get door',
     'opinion jada kusruthi lovabl silent spl charact matur stylish simpl pl repli',
     'hmmm thought said hour slave late punish',
     'beerag',
     'import custom servic announc premier call freephon',
     'dont think turn like randomlli within min open',
     'suppos make still town though',
     'time fix spell sometim get complet diff word go figur',
     'ever thought live good life perfect partner txt back name age join mobil commun p sm',
     'free top polyphon tone call nation rate get toppoli tune sent everi week text subpoli per pole unsub',
     'gud mrng dear hav nice day',
     'hope enjoy game yesterday sorri touch pl know fondli bein thot great week abiola',
     'e best ur drive tmr',
     'u dogbreath sound like jan c al',
     'omg want scream weigh lost weight woohoo',
     'gener one uncount noun u dictionari piec research',
     'realli get hang around',
     'orang custom may claim free camera phone upgrad loyalti call offer end thmarch c appli opt availa',
     'petey boy wherear friendsar thekingshead come canlov nic',
     'ok msg u b leav hous',
     'gimm lt gt minut ago',
     'last chanc claim ur worth discount voucher today text shop savamob offer mobil cs savamob pobox uz sub',
     'appt lt time gt fault u listen told u twice',
     'free st week nokia tone ur mobil everi week txt nokia get txting tell ur mate www getz co uk pobox w wq norm p tone',
     'guarante award even cashto claim ur award call free stop getstop php rg jx',
     'k',
     'dled imp',
     'sure make sure know smokin yet',
     'boooo alway work quit',
     'take half day leav bec well',
     'ugh wanna get bed warm',
     'nervou lt gt',
     'ring come guy costum gift futur yowif hint hint',
     'congratul ur award either cd gift voucher free entri weekli draw txt music tnc www ldew com win ppmx age',
     'borrow ur bag ok',
     'u outbid simonwatson shinco dvd plyr bid visit sm ac smsreward end bid notif repli end',
     'boytoy miss happen',
     'lot use one babe model help youi bring match',
     'also bring galileo dobbi',
     'respond',
     'boo babe u enjoyin yourjob u seem b gettin well hunni hope ure ok take care llspeak u soonlot lovem xxxx',
     'good afternoon starshin boytoy crave yet ach fuck sip cappuccino miss babe teas kiss',
     'road cant txt',
     'smsservic yourinclus text credit pl goto www comuk net login qxj unsubscrib stop extra charg help comuk cm ae',
     'p alfi moon children need song ur mob tell ur txt tone chariti nokia poli chariti poli zed profit chariti',
     'good even ttyl',
     'hmm bit piec lol sigh',
     'hahaha use brain dear',
     'hey got mail',
     'sorri light turn green meant anoth friend want lt gt worth may around',
     'thank yesterday sir wonder hope enjoy burial mojibiola',
     'u secret admir reveal think u r special call opt repli reveal stop per msg recd cust care',
     'hi mate rv u hav nice hol messag say hello coz sent u age start drive stay road rvx',
     'dear voucher holder claim week offer pc pleas go http www e tlp co uk expressoff ts cs appli stop text txt stop',
     'thank much skype wit kz sura didnt get pleasur compani hope good given ultimatum oh countin aburo enjoy messag sent day ago',
     'sure result offer',
     'good morn dear great amp success day',
     'want anytim network min text new video phone five pound per week call repli deliveri tomorrow',
     'sir late pay rent past month pay lt gt charg felt would inconsider nag someth give great cost didnt speak howev recess wont abl pay charg month henc askin well ahead month end pleas help thank',
     'tri contact offer new video phone anytim network min half price rental camcord call repli deliveri wed',
     'last chanc claim ur worth discount voucher text ye savamob member offer mobil cs sub remov txt x stop',
     'luv u soo much u understand special u r ring u morrow luv u xxx',
     'pl send comprehens mail pay much',
     'prashanthettan mother pass away last night pray famili',
     'urgent call landlin complimentari ibiza holiday cash await collect sae cs po box sk wp ppm',
     'k k go',
     'meanwhil shit suit xavier decid give us lt gt second warn samantha come play jay guitar impress shit also think doug realiz live anymor',
     'stomach thru much trauma swear eat better lose weight',
     'offic what matter msg call break',
     'yeah bare enough room two us x mani fuck shoe sorri man see later',
     'today offer claim ur worth discount voucher text ye savamob member offer mobil cs sub unsub repli x',
     'u reach orchard alreadi u wan go buy ticket first',
     'real babi want bring inner tigress',
     'da run activ full version da',
     'ah poor babi hope urfeel bettersn luv probthat overdos work hey go care spk u sn lot lovejen xxx',
     'stop stori told return say order',
     'talk sexi make new friend fall love world discreet text date servic text vip see could meet',
     'go take babe',
     'hai ana tomarrow come morn lt decim gt ill sathi go rto offic repli came home',
     'spoon okay',
     'say somebodi name tampa',
     'work go min',
     'brother geniu',
     'sorri guess whenev get hold connect mayb hour two text',
     'u find time bu coz need sort stuff',
     'dude ive see lotta corvett late',
     'congratul ur award either yr suppli cd virgin record mysteri gift guarante call ts cs www smsco net pm approx min',
     'consid wall bunker shit import never play peac guess place high enough matter',
     'privat account statement xxxxxx show un redeem point call identifi code expir',
     'hello need posh bird chap user trial prod champney put need address dob asap ta r',
     'u want xma free text messag new video phone half price line rental call free find',
     'well offici philosoph hole u wanna call home readi save',
     'go good problem still need littl experi understand american custom voic',
     'text drop x',
     'ugh long day exhaust want cuddl take nap',
     'talk atleast day otherwis miss best friend world shakespear shesil lt gt',
     'shop till u drop either k k cash travel voucher call ntt po box cr bt fixedlin cost ppm mobil vari',
     'castor need see someth',
     'sunshin quiz wkli q win top soni dvd player u know countri liverpool play mid week txt ansr sp tyron',
     'u secret admir look make contact u find r reveal think ur special call',
     'u secret admir look make contact u find r reveal think ur special call stopsm',
     'remind download content alreadi paid goto http doit mymobi tv collect content',
     'see knew give break time woul lead alway want miss curfew gonna gibe til one midnight movi gonna get til need come home need getsleep anyth need b studdi ear train',
     'love give massag use lot babi oil fave posit',
     'dude go sup',
     'yoyyooo u know chang permiss drive mac usb flash drive',
     'gibb unsold mike hussey',
     'like talk pa abl dont know',
     'dun cut short leh u dun like ah fail quit sad',
     'unbeliev faglord',
     'wife knew time murder exactli',
     'ask princess',
     'great princess think',
     'nutter cutter ctter cttergg cttargg ctargg ctagg ie',
     'ok noe u busi realli bore msg u oso dunno wat colour choos one',
     'g class earli tomorrow thu tri smoke lt gt',
     'superb thought grate u dont everyth u want mean u still opportun happier tomorrow u today',
     'hope good week check',
     'use hope agent drop sinc book thing year whole boston nyc experi',
     'thursday night yeah sure thing work',
     'free rington wait collect simpli text password mix verifi get usher britney fml',
     'probabl money worri thing come due sever outstand invoic work two three month ago',
     'possibl teach',
     'wonder phone batteri went dead tell love babe',
     'love smell bu tobacco',
     'get worri derek taylor alreadi assum worst',
     'hey charl sorri late repli',
     'lastest stereophon marley dizze racal libertin stroke win nookii game flirt click themob wap bookmark text wap',
     'give plu said grinul greet whenev speak',
     'white fudg oreo store',
     'januari male sale hot gay chat cheaper call nation rate p min cheap p min peak stop text call p min',
     'love come took long leav zaher got word ym happi see sad left miss',
     'sorri hurt',
     'feel nauseou piss eat sweet week caus today plan pig diet week hungri',
     'ok lor earli still project meet',
     'call da wait call',
     'could ask carlo could get anybodi els chip',
     'actual send remind today wonder weekend',
     'peopl see msg think iam addict msging wrong bcoz know iam addict sweet friend bslvyl',
     'hey gave photo regist drive ah tmr wanna meet yck',
     'dont talk ever ok word',
     'u wana see',
     'way school pl send ashley number',
     'shall fine avalarr hollalat',
     'went attend anoth two round today still reach home',
     'actual delet old websit blog magicalsong blogspot com',
     'k wait chikku il send aftr lt gt min',
     'diet ate mani slice pizza yesterday ugh alway diet',
     'k give kvb acc detail',
     'oh come ah',
     'money r lucki winner claim prize text money million give away ppt x normal text rate box w jy',
     'realli sorri b abl friday hope u find altern hope yr term go ok',
     'congratul ore mo owo wa enjoy wish mani happi moment fro wherev go',
     'samu shoulder yet',
     'time think need know near campu',
     'dear matthew pleas call landlin complimentari lux tenerif holiday cash await collect ppm sae cs box sk xh',
     'dun wear jean lor',
     'sinc side fever vomitin',
     'k k colleg',
     'urgent call landlin complimentari tenerif holiday cash await collect sae cs box hp yf ppm',
     'better made friday stuf like pig yesterday feel bleh least writh pain kind bleh',
     'sell ton coin sell coin someon thru paypal voila money back life pocket',
     'theyr lot place hospit medic place safe',
     'get touch folk wait compani txt back name age opt enjoy commun p sm',
     'also sorta blown coupl time recent id rather text blue look weed',
     'sent score sopha secondari applic school think think appli research cost also contact joke ogunrind school one less expens one',
     'cant wait see photo use',
     'ur cash balanc current pound maxim ur cash send go p msg cc po box tcr w',
     'hey book kb sat alreadi lesson go ah keep sat night free need meet confirm lodg',
     'chk ur belovd ms dict',
     'time want come',
     'awesom lemm know whenev around',
     'shb b ok lor thanx',
     'beauti truth graviti read care heart feel light someon feel heavi someon leav good night',
     'also rememb get dobbi bowl car',
     'filthi stori girl wait',
     'sorri c ur msg yar lor poor thing one night tmr u brand new room sleep',
     'love decis feel could decid love life would much simpler less magic',
     'welp appar retir',
     'sort code acc bank natwest repli confirm sent right person',
     '',
     'u sure u take sick time',
     'urgent tri contact u today draw show prize guarante call land line claim valid hr',
     'watch cartoon listen music amp eve go templ amp church u',
     'yo chad gymnast class wanna take site say christian class full',
     'much buzi',
     'better still catch let ask sell lt gt',
     'sure night menu know noon menu',
     'u want come back beauti necklac token heart that give wife like see one give dont call wait till come',
     'will go aptitud class',
     'wont b tri sort hous ok',
     'yar lor wan go c hors race today mah eat earlier lor ate chicken rice u',
     'haha awesom omw back',
     'yup thk e shop close lor',
     'account number',
     'eh u send wrongli lar',
     'hey ad crap nite borin without ya boggi u bore biatch thanx u wait til nxt time il ave ya',
     'ok shall talk',
     'dont hesit know second time weak like keep notebook eat day anyth chang day sure noth',
     'hey pay salari de lt gt',
     'anoth month need chocol weed alcohol',
     'start search get job day great potenti talent',
     'reckon need town eightish walk carpark',
     'congrat mobil g videophon r call videochat wid mate play java game dload polyph music nolin rentl',
     'look fuckin time fuck think',
     'yo guess drop',
     'carlo say mu lt gt minut',
     'offic call lt gt min',
     'geeee miss alreadi know think fuck wait till next year togeth love kiss',
     'yun ah ubi one say wan call tomorrow call look iren ere got bu ubi cre ubi tech park ph st wkg day n',
     'ugh gotta drive back sd la butt sore',
     'th juli',
     'hi im relax time ever get everi day parti good night get home tomorrow ish',
     'wan come come lor din c stripe skirt',
     'xma stori peac xma msg love xma miracl jesu hav bless month ahead amp wish u merri xma',
     'number',
     'chang e one next escal',
     'yetund class run water make ok pl',
     'lot happen feel quiet beth aunt charli work lot helen mo',
     'wait bu stop aft ur lect lar dun c go get car come back n pick',
     'aight thank comin',
     ...]




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


