# file-comparison-service
Spring Boot service for comparing content of different files like Excel, Text, CSV, PDF, etc, which provide JSON response of Match/Mismatch values

## Steps

* Build project:

  `mvn clean install`


* In local:

    * Run service :

      `mvn spring-boot:run`


* In docker container:

    * Build project: will use the FileComparisonService.jar created above

      `docker build -f Dockerfile -t file-comparison-service .`
    
    * Run service:

      `docker run -p 8100:8100 file-comparison-service`



* Use REST client like Postman and make POST Call:

    * Below request helps to extract text content from PDF file:
        * Method: `POST`
          
        * URL: `http://localhost:8100/api/v1/compare/excel`
          
        * Body: form-data
          
          Param: `actualFile`, Value: `actualFile_Excel.xlsx`
          
          Param: `expectedFile`, Value: `expectedFile_Excel.xlsx`
          
          Param: `fileType`, Value: `excel`
    
    * Response:


          `[
              {
              "Sheet Name": "English",
              "A1": "PASS",
              "A4": "PASS",
              "A5": "PASS",
              "A6": "PASS",
              "C9": "PASS",
              "A9": "PASS",
              "A10": "PASS",
              "B10": "PASS",
              "A11": "PASS",
              "D10": "PASS",
              "B11": "PASS",
              "C11": "PASS",
              "A12": "PASS",
              "C10": "PASS",
              "B12": "PASS",
              "A13": "FAIL - Expected value: Financial Account vs Actual value: Finance",
              "D12": "PASS",
              "B13": "PASS",
              "C13": "PASS",
              "A14": "PASS",
              "D11": "PASS",
              "C12": "FAIL - Expected value: 1.08 vs Actual value: 1.09",
              "B14": "FAIL - Expected value: 2.83 vs Actual value: 2.87",
              "D14": "PASS",
              "D13": "PASS",
              "C14": "PASS",
              "B1": "PASS",
              "B4": "PASS",
              "B5": "PASS",
              "B6": "FAIL - Expected value: 3.38 vs Actual value: 3.37",
              "D9": "PASS",
              "B9": "PASS"
              },
              {
              "Sheet Name": "日本語",
              "A1": "PASS",
              "A4": "PASS",
              "A5": "PASS",
              "A6": "PASS",
              "C9": "PASS",
              "A9": "PASS",
              "A10": "PASS",
              "B10": "PASS",
              "A11": "PASS",
              "D10": "PASS",
              "B11": "PASS",
              "C11": "PASS",
              "A12": "PASS",
              "C10": "PASS",
              "B12": "PASS",
              "A13": "FAIL - Expected value: 財務アカウント vs Actual value: 金融",
              "D12": "PASS",
              "B13": "PASS",
              "C13": "PASS",
              "A14": "PASS",
              "D11": "PASS",
              "C12": "FAIL - Expected value: 1.08 vs Actual value: 1.09",
              "B14": "FAIL - Expected value: 2.83 vs Actual value: 2.87",
              "D14": "PASS",
              "D13": "PASS",
              "C14": "PASS",
              "B1": "PASS",
              "B4": "PASS",
              "B5": "PASS",
              "B6": "FAIL - Expected value: 3.38 vs Actual value: 3.37",
              "D9": "PASS",
              "B9": "PASS"
              }
          ]`

    
