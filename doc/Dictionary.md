## Dictionary

### Structure of dictionary

- Header - FROM_LANGUAGE;TO_LANGUAGE (eg. "ENGLISH;POLISH")
- Translations - LEMMA;REPRESENTATION;TRANSLATION (eg. "eat;eat, ate, eaten;jeść")

### Free dict

- Download files form https://github.com/freedict/fd-dictionaries/. 
  For example to download english-polish dictionary download all
  files in directory https://github.com/freedict/fd-dictionaries/tree/master/eng-pol/letters.
  Each dictionary has licence in header of *.tei file (e.g. https://github.com/freedict/fd-dictionaries/blob/master/eng-pol/eng-pol.tei)
- Run script `script/parse_freedict.sh` for each file (e.g. `cat a.xml | ./parse_freedict.sh`) 
