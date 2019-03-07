# Añotador: servicio de anotación temporal para el español

To deploy locally using Docker:
* Clone the repo: `git clone https://github.com/mnavasloro/annotador.git `
* Build the docker image: `docker build -t annotador .`
* Launch a container: `docker rm annotadorCTR; docker run -p8080:8080 --name "annotadorCTR" -it annotador`
* Test: http://localhost:8080/swagger-ui.html#!/annotation/temporalUsingPOST
* Test:  `curl -X POST --header "Content-Type: application/json" --header "Accept: plain/text" -d "Mañana es día 3 de octubre" "http://localhost:8080/annotate/temporal?format=nif&language=es&dct=dd%2FMM%2Fyyyy"
`


Other interesting commands to keep at hand:
* docker run --rm -p8080:8080 --name "annotadorCTR" -it annotador bash (so you get to the container as bash,and you can run ls or other helpful programs)
* docker rm annotadorCTR (to delete)
* docker ps (-a) -> to see the containers (with -a it shows also the ones down)
* docker images -> to display the images
* Measure-Command {docker build -t annotador . | Write-Host} -> to measure the execution time