# Añotador: Temporal Tagging service for Spanish and English

## Demo
Please visit the demo (just for Spanish) in http://annotador.oeg-upm.net/.

## CURL/Postman
Here is also a curl example:

`curl -X POST "http://annotador.oeg-upm.net/annotate?inputText="Hoy%20es%20200%20de%20octubre"&inputDate="2019-04-09"&lan=ES`

You will find more examples in the postman collection: https://www.getpostman.com/collections/1caba0e49d40bd38789e

## Docker
To deploy locally using Docker:
* Clone the repo: `git clone https://github.com/mnavasloro/annotador.git `
* Build the docker image: `docker build -t annotador .`
* Launch a container: `docker rm annotadorCTR; docker run -p8080:8080 --name "annotadorCTR" -it annotador`
* Test: http://localhost:8080/swagger-ui.html#!/annotation/temporalUsingPOST
`


Other interesting commands to keep at hand:
* docker run --rm -p8080:8080 --name "annotadorCTR" -it annotador bash (so you get to the container as bash,and you can run ls or other helpful programs)
* docker rm annotadorCTR (to delete)
* docker ps (-a) -> to see the containers (with -a it shows also the ones down)
* docker images -> to display the images
* Measure-Command {docker build -t annotador . | Write-Host} -> to measure the execution time
  