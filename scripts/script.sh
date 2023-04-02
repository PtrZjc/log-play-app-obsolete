#!/bin/bash
# shellcheck disable=SC2002

input=$1
jq_script="jq.jq"

cat "$input" | 
	mlr --c2j cat |
	sd '""' 'null' |
	jq -f "$jq_script" 
