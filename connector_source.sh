#!/usr/bin/env bash
 curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d @./connectors/connector_mongo_links.json

