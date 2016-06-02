# Implementing a Web Indexer (Python)

## Requirements:
 * Preprocess the web page documents provided to select relevant terms to represent these documents.   *
 * All html tags should be cleaned, stop words be removed, and stemming be performed. 
## Libraries and APIs used:
### os
* os (Miscellaneous operating system interfaces) imported to read, create and write files 
* check os website (https://docs.python.org/2/library/os.html)
### re
* re (Regular expression operations) imported to check strings see if they match certain patterns
* check re website (https://docs.python.org/2/library/re.html)
### nltk
* NLTK is a leading platform for building Python programs to work with human language data.
* from nltk.corpus import stopwords; imported and downloaded from nltk.corpus for checking and removeing stopwords
* from nltk import PorterStemmer; imported PorterStemmer for stemming a single word
* check nltk website (http://www.nltk.org/)

## Review the key parts of code

### Removing html tags, words on the stop list, stemming, and any special handlings for better results
```python
    currentTextString = " ".join(thisFile.read().split())
    #store the file as a string for removing HTML tags
        
    textAfterHtmlRemovingString = re.sub('<[^>]*>', '', currentTextString) 
    # remove HTML tags (String)
        
    textAfterHtmlRemovingList = textAfterHtmlRemovingString.split() 
    # convert String to List for the text contains only characters
        
    textRemoveingUnnecessaryCharactersList = \
    [removeUnnecessaryCharacters(word) for word in textAfterHtmlRemovingList ] 

    textRemoveingUnnecessaryCharactersList = \
    [word for word in textRemoveingUnnecessaryCharactersList if word is not None]
    
    textAfterStopwordsRemovingList = \
    [word for word in textRemoveingUnnecessaryCharactersList if word not in \
    stopwords.words('english')] #remove stopwords

    stemmer = PorterStemmer() #stemming
        
    for eachWord in textAfterStopwordsRemovingList:
        eachWord = stemmer.stem(eachWord)
        storeToResultDict(eachWord,resultDict)
```
### Sort and display dictionaries
```python
def formatAndPrintResultDict(resultDict,destFile,rFile):
    resultList = \
    sorted(resultDict.iteritems(), key=lambda frequency:frequency[1], reverse = True) 
    # sort the dict based on the value (frequency)
    count = 1    
    for eachTuple in resultList:
        destFile.write('<tr><td>'+eachTuple[0]+'</td><td>'+str(eachTuple[1])+'</td></tr>')
        if count <= 200:
            rFile.write(eachTuple[0]+'    '+str(eachTuple[1])+'\n')
        if count == 200:
            destFile.write('<tr><td>Above is the top 200 most frequent words</td></tr>')
            rFile.write('Above is the top 200 most frequent words\n')
        count = count + 1
    destFile.write \
    ('<tr><td>There are totally '+str(count)+' words be identified</td></tr>')
    rFile.write('There are totally '+str(count)+' words be identified.\n')
    return resultList
```
### Calculate words exist in both set and only appear in one set
```python
def calculateWordsAppearInBothSetOrNot(r1,r2):
    s1 = set([(eachTuple[0]) for eachTuple in r1])
    s2 = set([(eachTuple[0]) for eachTuple in r2])
    return len(s1&s2),len(s1-s2)
```
## Any problems I have encountered and solved, and lessons I have learned
* Conversion between various data structures (List/Dictionary/Set)
* Get cleaner result (Solved by analyzing the words appear in previous result and then clean the unreasonable words)
## Results:
### How many words are identified from the training set
* There are totally 7646 words be identified in the training set.
### Present and discuss top 200 most frequent words identified
comput    (1116),
gmt    (848),
page    (812),
scienc    (726),
system    (660),
univers    (660),
home    (533),
server    (532),
date    (498),
program    (481),
austin    (469),
contenttyp    (465),
texthtml    (465),
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
email    (196),
current    (195),
oper    (187),
paper    (186),
time    (180),
cs    (179),
also    (178),
design    (175),
may    (170),
seattl    (169),
link    (169),
im    (155),
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
tx    (134),
updat    (133),
avail    (132),
note    (132),
compil    (129),
th    (128),
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
ta    (109),
phone    (108),
handout    (107),
document    (107),
wa    (105),
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
ut    (101),
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
pm    (79),
uw    (78),
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
html    (74),
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
go    (66),
www    (66),
box    (66),
artifici    (66),
first    (66),
stuff    (65),
imag    (65),
librari    (65),
lisp    (65),
pp    (65),
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
#### Example words that are not properly cleaned, e.g. sle"), il'-10, etc. Why were they difficult to get properly preprocessed? 
* Not found. Because special characters are removed.
#### How many words are identified from the test set?
* There are totally 5251 words be identified.
#### How many words appear in both the training set and the test set How many do not? 
* Appear in both: 3027 Not appear in one doc: 4618.
#### References
Python stopwords removing 
(http://stackoverflow.com/questions/5486337/how-to-remove-stop-words-using-nltk-or-python)

Stemming
(http://stackoverflow.com/questions/10369393/need-a-python-module-for-stemming-of-text-documents)

os
(https://docs.python.org/2/library/os.html)

re
(https://docs.python.org/2/library/re.html)