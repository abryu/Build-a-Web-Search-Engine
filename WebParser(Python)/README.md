# Implementing a Web Parser (Python)

## Requirements:
 * Preprocess the web page documents provided to select relevant terms to represent these documents.   
 * All html tags should be cleaned, stop words be removed, and stemming be performed. 

## Tools Used:
### Language: Python.
#### Why I use Python rather than Java?
* Easy syntax and readibility.
* Python programs are generally expected to run slower than Java programs, but they also take much less time to develop. 
* Python programs are typically 3-5 times shorter than equivalent Java programs. This difference can be attributed to Python's built-in high-level data types and its dynamic typing. 

### Built on Spyder.
* Spyder is an interactive Python development environment providing MATLAB-like features in a simple and light-weighted software. It also provides ready-to-use pure-Python widgets to your PyQt4 or PySide application: source code editor with syntax highlighting and code introspection/analysis features, NumPy array editor, dictionary editor, Python console, etc.

## Libraries and APIs used:

### os
* os (Miscellaneous operating system interfaces) imported to read, create and write files 
* check os website (https://docs.python.org/2/library/os.html)

### re
* re (Regular expression operations) imported to check strings see if they match certain patterns
* check re website (https://docs.python.org/2/library/re.html)

### nltk

#### NLTK is a leading platform for building Python programs to work with human language data. (http://www.nltk.org/)

#### from nltk.corpus import stopwords; imported and downloaded from nltk.corpus for checking and removing stopwords
*  Stop words are basically a set of commonly used words in any language, not just English. The reason why stop words are critical to many applications is that, if we remove the words that are very commonly used in a given language, we can focus on the important words instead.

#### from nltk import PorterStemmer; imported PorterStemmer for stemming a single word
* Stemming is the process for reducing inflected (or sometimes derived) words to their stem, base or root form—generally a written word form. 

## Review the key parts of code

### Removing html tags, words on the stop list, stemming, and any special handlings for better results
```python
def openAndProcessingFiles(path,resultDict):  # Main Function

    for filename in os.listdir(os.getcwd()+path):

        thisFile = open(os.getcwd()+path+'/'+filename,'r') # open the file and process each file
        
        currentTextString = " ".join(thisFile.read().split()) # store the file as a string for removing HTML tags
        
        textAfterHtmlRemovingString = re.sub('<[^>]*>', '', currentTextString) # remove HTML tags (String)
        
        textAfterHtmlRemovingList = textAfterHtmlRemovingString.split() 
        # convert String to List for the text contains only characters
        
        textRemoveingUnnecessaryCharactersList = \ 
        [removeUnnecessaryCharacters(word) for word in textAfterHtmlRemovingList ] 

        textRemoveingUnnecessaryCharactersList = \
        [word for word in textRemoveingUnnecessaryCharactersList if word is not None]
        
        stop_words = set(stopwords.words('english'))
        
        stop_words.update(['texthtml', 'html', 'server', "email", 'date', 'info']) 
        # By analying the previous result set, continully adding new stopwords words
    
        textAfterStopwordsRemovingList = \
        [word for word in textRemoveingUnnecessaryCharactersList if word not in stop_words] #remove stopwords

        stemmer = PorterStemmer() #stemming
        
        for eachWord in textAfterStopwordsRemovingList:
            eachWord = stemmer.stem(eachWord)
            storeToResultDict(eachWord,resultDict)
    
        thisFile.close()
```
### Remove special characters
```python
def removeUnnecessaryCharacters(word):
    if '@' in word or 'www' in word: # eliminate email addresses and website addresses
        return None
    newWord = "".join(re.findall("[a-zA-Z]+", word)).lower() # contain only characters
    if len(newWord) > 2 and len(newWord) < 15:            
        return newWord
    else:
        return None
```
### Sort and display dictionaries
```python
def formatAndPrintResultDict(resultDict,destFile,rFile):
    resultList= sorted(resultDict.iteritems(), key=lambda frequency:frequency[1], reverse = True) 
    # sort the dict based on the value (frequency)
    count = 1    
    for eachTuple in resultList:
        destFile.write('<tr><td>'+eachTuple[0]+'</td><td>'+str(eachTuple[1])+'</td></tr>')
        if count <= 200:
            rFile.write(eachTuple[0]+'    ('+str(eachTuple[1])+'),\n')
        if count == 200:
            destFile.write('<tr><td>Above is the top 200 most frequent words</td></tr>')
            rFile.write('Above is the top 200 most frequent words\n')
        count = count + 1
        
    destFile.write('<tr><td>There are totally '+str(count)+' words be identified</td></tr>')
    rFile.write('There are totally '+str(count)+' words be identified.\n')
    return resultList
```
### Calculate words exist in both set and only appear in one set. 
```python
def calculateWordsAppearInBothSetOrNot(r1,r2):
    s1 = set([(eachTuple[0]) for eachTuple in r1])
    s2 = set([(eachTuple[0]) for eachTuple in r2])
    return len(s1&s2),len(s1-s2)
```
### Store processed words/terms to result dict
```python
def storeToResultDict(word,resultDict):
    if resultDict.has_key(word): #this word exists, update the value =+ 1
        resultDict[word] = resultDict.get(word)+1
    else: # this word doesnt exist, add this word as a key and value = 1
        resultDict[word] = 1
```
## Any problems I have encountered and solved
* Conversion between various data structures (List/Dictionary/Set)
* Get cleaner result. (Solved by analyzing the words appear in previous result and then adding the unreasonable words to stopwords list) 
* Diffierent ways to tokeize the text. i.e. replacing stopwords by space, None or others. (Solved by replacing by None)
* Handle special characters. (Solved by removing strings that contain special characters. Because those strings are meaningless)

## Lessons I have learnt
* Building web parser.
* How to deal with dirty data/words.
* Data pre-processing.
* Data structures. List/Dictionary/Set for storing data and operations on those data.
* Python features, syntax.

## Results:

### How many words are identified from the training set
* There are totally 7646 words be identified in the training set.

### Present and discuss top 200 most frequent words identified:

For Training Set:

comput    (1116),
page    (812),
scienc    (726),
system    (660),
univers    (660),
home    (533),
program    (481),
austin    (469),
contenttyp    (465),
research    (431),
inform    (386),
lastmodifi    (382),
contentlength    (382),
depart    (377),
nov    (334),
cse    (331),
interest    (323),
work    (322),
monday    (321),
use    (312),
engin    (300),
cours    (287),
project    (281),
texa    (277),
jan    (267),
offic    (267),
web    (261),
class    (247),
student    (241),
thu    (240),
washington    (235),
mimevers    (232),
cern    (232),
parallel    (232),
softwar    (225),
ncsa    (223),
languag    (204),
last    (203),
assign    (198),
current    (195),
oper    (187),
paper    (186),
time    (180),
also    (178),
design    (175),
may    (170),
seattl    (169),
link    (169),
address    (154),
read    (153),
homework    (152),
algorithm    (151),
network    (150),
postscript    (147),
distribut    (146),
graduat    (144),
phd    (142),
list    (141),
schedul    (140),
solut    (135),
group    (135),
updat    (133),
avail    (132),
note    (132),
compil    (129),
public    (128),
professor    (126),
acm    (125),
contact    (125),
learn    (123),
file    (123),
hour    (123),
data    (121),
exam    (120),
problem    (119),
new    (117),
usa    (117),
sieg    (114),
one    (114),
gener    (113),
lectur    (113),
includ    (113),
multimedia    (112),
mathemat    (112),
proceed    (112),
mail    (110),
phone    (108),
handout    (107),
document    (107),
tuesday    (104),
tay    (104),
year    (103),
applic    (103),
fall    (103),
dec    (103),
oct    (103),
wednesday    (102),
topic    (102),
due    (102),
machin    (101),
architectur    (101),
hall    (100),
implement    (98),
final    (98),
see    (98),
support    (96),
perform    (96),
recent    (95),
model    (95),
confer    (94),
welcom    (92),
area    (91),
develop    (89),
intellig    (89),
spring    (88),
code    (87),
fax    (87),
get    (84),
databas    (84),
friday    (83),
analysi    (82),
studi    (80),
present    (80),
like    (79),
commun    (78),
quarter    (77),
onlin    (77),
taylor    (77),
provid    (76),
book    (76),
test    (76),
intern    (76),
process    (75),
nbsp    (75),
internet    (74),
version    (74),
instructor    (74),
april    (73),
vol    (72),
lab    (72),
object    (71),
logic    (71),
person    (71),
send    (71),
graphic    (71),
check    (71),
method    (70),
java    (69),
thursday    (69),
requir    (68),
ieee    (68),
click    (68),
construct    (68),
interfac    (68),
manag    (67),
number    (67),
search    (67),
place    (67),
set    (66),
technolog    (66),
look    (66),
box    (66),
artifici    (66),
first    (66),
stuff    (65),
imag    (65),
librari    (65),
lisp    (65),
pleas    (65),
extens    (64),
plan    (64),
well    (64),
pictur    (64),
associ    (64),
sep    (63),
educ    (62),
issu    (62),
memori    (62),
servic    (61),
find    (61),
modifi    (60),
meet    (60),
autumn    (60),
homepag    (59),
format    (59),
syllabu    (59),
octob    (59),
bershad    (59),
world    (58),
move    (58),
thing    (58),
workshop    (58),
environ    (58),
specif    (57),
academ    (57),
exampl    (57),
scheme    (57),
info    (57),
make    (57),
messag    (57),


For Test Set:

comput    (625),
system    (369),
page    (361),
scienc    (322),
program    (312),
cornel    (294),
univers    (261),
nov    (259),
lectur    (240),
cours    (238),
home    (228),
wednesday    (217),
use    (208),
contenttyp    (206),
contentlength    (205),
lastmodifi    (205),
mimevers    (203),
cern    (203),
research    (198),
project    (190),
work    (174),
inform    (153),
homework    (147),
depart    (139),
interest    (138),
languag    (138),
engin    (137),
student    (137),
distribut    (121),
upson    (116),
class    (113),
assign    (112),
algorithm    (111),
offic    (104),
also    (104),
problem    (102),
last    (99),
network    (98),
link    (98),
ithaca    (97),
web    (95),
develop    (91),
paper    (91),
note    (90),
tuesday    (89),
solut    (87),
process    (86),
softwar    (86),
may    (84),
thursday    (83),
avail    (82),
fall    (82),
new    (80),
time    (80),
oper    (80),
current    (79),
text    (78),
parallel    (75),
group    (72),
databas    (71),
one    (71),
theori    (70),
design    (70),
due    (70),
final    (70),
graphic    (68),
video    (68),
hour    (66),
phd    (65),
hall    (63),
data    (62),
model    (62),
implement    (62),
grade    (61),
novemb    (61),
applic    (60),
document    (60),
report    (59),
like    (59),
multimedia    (58),
master    (57),
gener    (57),
materi    (57),
proceed    (55),
graduat    (55),
sunday    (54),
monday    (53),
confer    (52),
read    (52),
list    (52),
public    (52),
spring    (52),
includ    (52),
lab    (51),
postscript    (51),
compil    (51),
acm    (51),
machin    (51),
structur    (51),
analysi    (50),
prelim    (50),
intern    (50),
symposium    (50),
get    (49),
architectur    (49),
version    (49),
code    (49),
topic    (48),
introduct    (48),
pleas    (48),
oct    (47),
septemb    (47),
homepag    (47),
modifi    (47),
construct    (47),
imag    (47),
exam    (47),
well    (47),
professor    (46),
octob    (46),
java    (46),
see    (46),
site    (46),
click    (45),
techniqu    (44),
two    (44),
teach    (44),
part    (44),
set    (44),
technolog    (44),
retriev    (44),
robot    (44),
member    (44),
school    (43),
object    (43),
first    (43),
activ    (43),
onlin    (43),
phone    (42),
tool    (42),
file    (42),
question    (42),
april    (42),
year    (41),
resum    (41),
commun    (41),
center    (41),
handout    (40),
relat    (40),
associ    (40),
section    (40),
advanc    (40),
journal    (40),
type    (39),
ieee    (39),
learn    (39),
mathemat    (39),
updat    (38),
optim    (38),
high    (38),
logic    (37),
make    (37),
info    (37),
foundat    (37),
studi    (36),
protocol    (36),
appear    (36),
vision    (36),
juli    (36),
committe    (36),
welcom    (36),
environ    (35),
method    (35),
address    (35),
name    (35),
perform    (34),
support    (34),
take    (34),
area    (34),
start    (34),
cover    (34),
annual    (34),
von    (34),
recent    (34),
internet    (33),
talk    (33),
assist    (33),
fax    (33),
friday    (33),
person    (33),
world    (33),
approxim    (33),
technic    (32),
complet    (32),
peopl    (32),
look    (32),
access    (32),
eicken    (31),
practic    (31),
nation    (31),

#### Example words that are not properly cleaned, e.g. sle"), il'-10, etc. Why were they difficult to get properly preprocessed? 
* Not found. Because words contain special characters are removed. Those words are meaningless.

#### How many words are identified from the test set?
* There are totally 5251 words be identified.

#### How many words appear in both the training set and the test set How many do not? 
* Appear in both: 3027 Not appear in one doc: 4618.

#### Detailed result please check (create a new local html file, copy the code to your html file and open it with a browser)
(https://github.com/abryu/Build-a-Web-Search-Engine/blob/master/WebParser(Python)/result.txt) 
(https://github.com/abryu/Build-a-Web-Search-Engine/blob/master/WebParser(Python)/destinationTestFile.html)
(https://github.com/abryu/Build-a-Web-Search-Engine/blob/master/WebParser(Python)/destinationTrainingFile.html)


#### References

Python Advantages
(https://www.python.org/doc/essays/comparisons/)
(https://en.wikiversity.org/wiki/Python/Why_learn_Python)

More about Spyder
(https://pythonhosted.org/spyder/overview.html)

Stopwords
(http://nlp.stanford.edu/IR-book/html/htmledition/dropping-common-terms-stop-words-1.html)

Python stopwords removing 
(http://stackoverflow.com/questions/5486337/how-to-remove-stop-words-using-nltk-or-python)

Stemming
(http://stackoverflow.com/questions/10369393/need-a-python-module-for-stemming-of-text-documents)
(http://www.nltk.org/api/nltk.stem.html)
(http://textminingonline.com/dive-into-nltk-part-iv-stemming-and-lemmatization)

os
(https://docs.python.org/2/library/os.html)

re
(https://docs.python.org/2/library/re.html)

dictionary sorting
(http://stackoverflow.com/questions/72899/how-do-i-sort-a-list-of-dictionaries-by-values-of-the-dictionary-in-python)

add new stopwords
(http://stackoverflow.com/questions/19130512/stopword-removal-with-nltk)
