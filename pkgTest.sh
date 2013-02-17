
#!/bin/sh

version=0.1

rm -rf search-*

mvn clean appassembler:assemble package -DskipTests


unzip search-0.1.zip

search

