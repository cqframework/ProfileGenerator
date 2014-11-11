mvn install:install-file -Dfile=localjars/org.hl7.fhir.tools.jar -DgroupId=fhir.me -DartifactId=tools -Dversion=0.8 -Dpackaging=jar
mvn install:install-file -Dfile=localjars/fhir-dev.jar -DgroupId=fhir.me -DartifactId=devtools -Dversion=0.8 -Dpackaging=jar
