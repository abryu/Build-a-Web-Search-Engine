import os # import os for reading source files
import re # import re (regular expression) for checking strings 
          # to see if they match certain patterns
from nltk.corpus import stopwords # import and download from nltk.corpus for checking and removeing stopwords
from nltk import PorterStemmer # import PorterStemmer for stemming a single word

trainingPathForCourse = '/train/course'
trainingPathForFaculty = '/train/faculty'
trainingPathForStudent = '/train/student'

testPathForCourse = '/test/course'
testPathForFaculty = '/test/faculty'
testPathForStudent = '/test/student'

# dictionaries for storing result
storingTrainingSetResult = {}
storingTestSetResult = {}

def calculateWordsAppearInBothSetOrNot(r1,r2):
    s1 = set([(eachTuple[0]) for eachTuple in r1])
    s2 = set([(eachTuple[0]) for eachTuple in r2])
    return len(s1&s2),len(s1-s2)

def removeUnnecessaryCharacters(word):
    if '@' in word or 'www' in word: # eliminate email addresses and website addresses
        return None
    newWord = "".join(re.findall("[a-zA-Z]+", word)).lower() # contain only characters
    if len(newWord) > 2 and len(newWord) < 15:            
        return newWord
    else:
        return None

def storeToResultDict(word,resultDict):
    if resultDict.has_key(word): #this word exists, update the value =+ 1
        resultDict[word] = resultDict.get(word)+1
    else: # this word doesnt exist, add this word as a key and value = 1
        resultDict[word] = 1
    
def formatAndPrintResultDict(resultDict,destFile,rFile):
    resultList= sorted(resultDict.iteritems(), key=lambda frequency:frequency[1], reverse = True) # sort the dict based on the value (frequency)
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

def openAndProcessingFiles(path,resultDict):  # Main Function

    for filename in os.listdir(os.getcwd()+path):

        thisFile = open(os.getcwd()+path+'/'+filename,'r') #open the file and process each file
        
        currentTextString = " ".join(thisFile.read().split())#store the file as a string for removing HTML tags
        
        textAfterHtmlRemovingString = re.sub('<[^>]*>', '', currentTextString) # remove HTML tags (String)
        
        textAfterHtmlRemovingList = textAfterHtmlRemovingString.split() # convert String to List for the text contains only characters
        
        textRemoveingUnnecessaryCharactersList = [removeUnnecessaryCharacters(word) for word in textAfterHtmlRemovingList ] 

        textRemoveingUnnecessaryCharactersList = [word for word in textRemoveingUnnecessaryCharactersList if word is not None]
        
        stop_words = set(stopwords.words('english'))
        
        stop_words.update(['texthtml', 'html', 'server', "email", 'date', 'gmt', 'www']) # By analying the previous result set, continully adding new stopwords
    
        textAfterStopwordsRemovingList = [word for word in textRemoveingUnnecessaryCharactersList if word not in stop_words] #remove stopwords

        stemmer = PorterStemmer() #stemming
        
        for eachWord in textAfterStopwordsRemovingList:
            eachWord = stemmer.stem(eachWord)
            storeToResultDict(eachWord,resultDict)
    
        thisFile.close()
        
f = open('destinationTrainingFile.html','w') #create and write to a file for storing Training set results
f2 = open('destinationTestFile.html','w') #create and write to a file for storing Test set results

resultFile = open('result.txt','w') # create and write to file that storing the result

f.write('<!DOCTYPE html><html><body><table>\
    <tr><th>Word List</th><th>Frequency</th></tr>') #write the header to the destination file
f2.write('<!DOCTYPE html><html><body><table>\
    <tr><th>Word List</th><th>Frequency</th></tr>') #write the header to the destination file

openAndProcessingFiles(trainingPathForCourse,storingTrainingSetResult)
openAndProcessingFiles(trainingPathForFaculty,storingTrainingSetResult)
openAndProcessingFiles(trainingPathForStudent,storingTrainingSetResult)

openAndProcessingFiles(testPathForCourse,storingTestSetResult)
openAndProcessingFiles(testPathForFaculty,storingTestSetResult)
openAndProcessingFiles(testPathForStudent,storingTestSetResult)

intersectionAndDifference = (calculateWordsAppearInBothSetOrNot(formatAndPrintResultDict(storingTrainingSetResult,f,resultFile),formatAndPrintResultDict(storingTestSetResult,f2,resultFile)))

resultFile.write('Appear in both: '+str(intersectionAndDifference[0])+' Not appear in one doc: '+str(intersectionAndDifference[1]))

f.write('</table></body></html>') # End of the file, write the close tags
f.close()
f2.write('</table></body></html>') # End of the file, write the close tags
f2.close()
resultFile.close()