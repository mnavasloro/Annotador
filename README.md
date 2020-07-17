# Añotador: Temporal Tagging service for Spanish and English

## Demo
Please visit the demo (just for Spanish) in https://annotador.oeg.fi.upm.es/

For documentation on the code, please visit https://mnavasloro.github.io/Annotador/

## CURL/Postman
Here is also a curl example:

`curl --location --request POST 'https://annotador.oeg.fi.upm.es/annotate' \`

`--header 'Content-Type: application/json;charset=utf-8' \`

`--data-raw '{`

`    "inputText": "—Todos ustedes, Zombies— en el año dos mil narra la crónica de un hombre joven.",`
    
`    "inputDate": "",`
    
`    "domain": "standard",`
    
`    "lan": "es"`
`}'`

The structure of the expected json is the following:

`{`

`    "inputText": String with the text to tag,`
    
`    "inputDate": (optional) Date of reference YYYY-MM-DD,`
    
`    "domain": (optional) domain of the text, among "Standard" (default) or "Legal",`
    
`    "lan": (optional) language, "es" (Spanish, default) or "en" (English),`
    
`	"format": (optiona) "timex3" (default) or "json"`
	
`}`



You will find more examples in the postman collection: https://www.getpostman.com/collections/3a2492f833535edf39b1

## Docker
To deploy locally using Docker:
* Clone the repo: `git clone https://github.com/mnavasloro/annotador.git `
* Build the docker image: `docker build -t annotador .`
* Launch a container: `docker rm annotadorCTR; docker run -p8080:8080 --name "annotadorCTR" -it annotador`
* Test: http://localhost:8080/swagger-ui.html#!/annotation/temporalUsingPOST
`


Other interesting commands to keep at hand:
* `docker run --rm -p8080:8080 --name "annotadorCTR" -it annotador bash` (so you get to the container as bash,and you can run ls or other helpful programs)
* `docker rm annotadorCTR` (to delete)
* `docker ps (-a)` -> to see the containers (with -a it shows also the ones down)
* `docker images` -> to display the images
* `Measure-Command {docker build -t annotador . | Write-Host}` -> to measure the execution time
  
