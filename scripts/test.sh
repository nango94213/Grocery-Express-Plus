#!/usr/bin/env bash

SCENARIO=$1

mkdir -p ./docker_results
docker run --name dronedelivery gatech/dronedelivery sh -c "\
    java -jar drone_delivery.jar < commands_${SCENARIO}.txt > drone_delivery_${SCENARIO}_results.txt && \
    diff -s drone_delivery_${SCENARIO}_results.txt drone_delivery_initial_${SCENARIO}_results.txt > diff_results_${SCENARIO}.txt && \
    cat diff_results_${SCENARIO}.txt"
docker cp dronedelivery:/usr/src/cs6310/drone_delivery_${SCENARIO}_results.txt ./docker_results/
docker cp dronedelivery:/usr/src/cs6310/diff_results_${SCENARIO}.txt ./docker_results
docker rm dronedelivery > /dev/null

