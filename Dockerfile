FROM openjdk:11
ADD target/FileComparisonService.jar FileComparisonService.jar
EXPOSE 8100
ENTRYPOINT ["java","-jar","FileComparisonService.jar"]
