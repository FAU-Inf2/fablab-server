
HMAC=`cat ../src/dist/tuerstatus-appserver.key`
STATE=$1
TIME=`date +%s`

if [ -z "$STATE" ]; then
	exit 1;
fi

MESSAGE=$TIME:$STATE
HASH=`echo -n $MESSAGE | openssl sha256 -hmac "$HMAC" | cut -d " " -f 2`
URL="https://52.28.16.59/spaceapi/update/?data=$MESSAGE&hash=$HASH"
#URL="http://localhost:8080/spaceapi/update/?data=$MESSAGE&hash=$HASH"

echo message: $MESSAGE
echo hash: $HASH
echo url: $URL

wget --no-check-certificate -O - "$URL"
