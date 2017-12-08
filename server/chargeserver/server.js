var express = require('express');
var bodyParser = require('body-parser')
var request = require('request');
var async = require('async');
var mongoose = require('mongoose');
var app = express();
var fs = require('fs');
var routing = require('./routing.js')
const camelcaseKeys = require('camelcase-keys');

var googleAPIKey = "AIzaSyBOdIvZrtOfFZeAGq2RTegybEaFEeUcltI";
var googleServerAPIKey = "AIzaSyDhY7IqIIk6jdj1wtKT-PyNIZRrsC6lM30";
var googleMapsClient = require('@google/maps').createClient({
    key: googleServerAPIKey
});
mongoose.connect('mongodb://localhost/ecarcharger', { useMongoClient: true });
var reservationSchema = new mongoose.Schema({
    chargerId: String,
    userId: String,
    from: Number,
    to:Number
});
var settingsSchema = new mongoose.Schema({
    userId: String,
    distance: Number,
    connectortype: Number
});

var reservationModel = mongoose.model('Reservation', reservationSchema)
var settingsModel = mongoose.model('Settings', settingsSchema)

var bufferedPoints = []
var bufferedResources = {}


app.use('/openchargemap', function (req, res) {
    var ocmBaseUrl = "https://api.openchargemap.io"
    var forward = req.originalUrl.replace(req.baseUrl, '')
    var options = {
        url: ocmBaseUrl + forward,
        method: 'GET'
    }
    request(options, (err, response, body) => {
        res.setHeader('Content-Type', 'application/json')
        res.send(body);
    })

});

app.get('/stations', function (req, res) {
    if (req.query.latitude && req.query.longitude && req.query.distance) {
        routing.getPointsInDistance(
            bufferedPoints,
            { latitude: parseFloat(req.query.latitude), longitude: parseFloat(req.query.longitude) },
            parseFloat(req.query.distance),
            (err, points) => {
                if (req.query.maxresults) {
                    var maxresults = parseInt(req.query.maxresults)
                    if (maxresults < points.length) {
                        points = points.slice(0, maxresults)
                    }
                }
                res.setHeader('Content-Type', 'application/json')
                res.end(JSON.stringify(points))
            }
        )
    }
    else {
        res.setHeader('Content-Type', 'application/json')
        res.end(JSON.stringify([]))
    }
})
app.get('/reservation', function (req, res) {
    reservationModel.find(req.query, (err, data) => {
        res.setHeader('Content-Type', 'application/json')
        if (err) {
            res.write(JSON.stringify({ success: false, error: err }))
            
        }
        else {
            res.write(JSON.stringify({ success: true, data: data }))
        }
        res.end()
    })
})
app.post('/reservation', bodyParser.json(), function (req, res) {
    var reservation = new reservationModel()
    for (var i in req.body) {
        reservation[i] = req.body[i]
    }
    reservation.save((err) => {
        res.setHeader('Content-Type', 'application/json')
        if (err) {
            res.write(JSON.stringify({ success: false, error: err }))
          
        }
        else {
            res.write(JSON.stringify({ success: true, data: [reservation] }))
        }
        res.end()
    })
})
app.put('/reservation', bodyParser.json(), function (req, res) {
    async.waterfall(
        [
            (callback) => {
                var id = req.body['_id']
                delete req.body['_id']
                reservationModel.updateOne({ _id: id }, req.body, callback)
            }
        ],
        (err, data) => {
            res.setHeader('Content-Type', 'application/json')
            if (err) {
                res.write(JSON.stringify({ success: false, error: err }))
            }
            else {
                res.write(JSON.stringify({ success: true, data: data })) 
            }
            res.end()
        }
    )

})
app.delete('/reservation', bodyParser.json(), function (req, res) {
    reservationModel.deleteMany(req.body, (err) => {
        res.setHeader('Content-Type', 'application/json')
        if (err) {
            res.write(JSON.stringify({ success: false, error: err }))
        }
        else {
            res.write(JSON.stringify({ success: true, data: [] }))
        }
        res.end()
    })
})
app.post('/maps/route', bodyParser.json(), (req, res) => {
    /*var options = {
        url: "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood4&key="+googleAPIKey,
        method: 'GET'
    }
    request(options, (err, response, body) => {
        res.setHeader('Content-Type', 'application/json')
        res.send(body);
    })*/
   /* var par = {
        dist: 100000,
        start: {
            latitude: 47.474897,
            longitude: 19.053558
        },
        end: {
            latitude: 47.952203,
            longitude: 21.720380
        }
    }*/
    var par = req.body
    routing.planRoute(bufferedPoints, par, (err, wp) => {
        var googleWaypoints = []
        for (var i = 0; i < wp.length; i++) {
            googleWaypoints.push({ lat: wp[i].latitude, lng: wp[i].longitude })
        }
        var callOptions = {
            origin: googleWaypoints[0],
            destination: googleWaypoints[googleWaypoints.length - 1],
            waypoints: googleWaypoints,
            units: "metric"
        }
        /*res.setHeader('Content-Type', 'application/json')
        res.end(JSON.stringify(callOptions));*/
        googleMapsClient.directions(
            callOptions,
            (err, result) => {
                res.setHeader('Content-Type', 'application/json')
                res.send(camelcaseKeys(result.json, { deep: true }));
            }
        )
    })
})

app.get('/settings', (req, res) => {
    settingsModel.find(req.query, (err, data) => {
        res.setHeader('Content-Type', 'application/json')
        if (err) {
            res.write(JSON.stringify({ success: false, error: err }))

        }
        else {
            res.write(JSON.stringify({ success: true, data: data }))
        }
        res.end()
    })
})
app.post('/settings', bodyParser.json(), (req, res) => {
    var settings = new settingsModel()
    for (var i in req.body) {
        settings[i] = req.body[i]
    }
    settings.save((err) => {
        res.setHeader('Content-Type', 'application/json')
        if (err) {
            res.write(JSON.stringify({ success: false, error: err }))

        }
        else {
            res.write(JSON.stringify({ success: true, data: [settings] }))
        }
        res.end()
    })
})
app.use('/', function (req, res) {
    res.end('server live')
})
var openChargeMapQuery = (params, callback) => {
    var url = "https://api.openchargemap.io/v2/poi/?output=json"
    for (var i in params) {
        url += "&" + i + "=" + params[i]
    }
    var options = {
        url: url,
        method: 'GET'
    }
    request(options, (err, response, body) => {
        callback(err, body)
    })
}
var startServer = () => {
    var port = 3000
    app.listen(port);
    console.log(bufferedPoints.length)
    console.log('server listening on port ' + port)
}



var bufferPointData = (callback) => {
    var resultLimit = 8000
    var distLimit = 1000 * 1000;
    var origo = {
        latitude: 47.1801155,
        longitude: 19.5039961
    }
    var bufferFile = "./buffer/hu.json"
    fs.access(bufferFile, (err) => {
        if (err) {
            console.log("download")
            async.series(
                [
                    (callback) => {
                        async.whilst(
                            () => {
                                if (bufferedPoints.length == 0) {
                                    return true;
                                }
                                if (resultLimit > bufferedPoints.length) {
                                    return false;
                                }
                                return true;
                            },
                            (callback) => {
                                resultLimit = resultLimit * 2;
                                console.log(resultLimit)
                                var par = {
                                    maxresults: resultLimit,
                                    latitude: origo.latitude,
                                    longitude: origo.longitude,
                                    distance: distLimit / 1000,
                                    distanceunit: "KM",
                                    //compact: true,
                                    //verbose: false
                                    compact: false
                                }
                                openChargeMapQuery(par, (err, result) => {
                                    if (err) { callback(err) }
                                    else {
                                        bufferedPoints = JSON.parse(result)
                                        callback()
                                    }

                                })
                            },
                            (err) => {
                                async.setImmediate(callback, err)
                            }
                        )
                    },
                    (callback) => {
                        fs.writeFile(bufferFile, JSON.stringify(bufferedPoints), callback)
                    }
                ],
                (err) => {
                    if (err) {
                        callback(err)
                    }
                    else { callback() }
                }
            )
        }
        else {
            console.log("fromfile")
            fs.readFile(bufferFile, 'utf8', (err, data) => {
                if (err) {
                    callback(err)
                }
                else {
                    bufferedPoints = JSON.parse(data)
                    //console.log(bufferedPoints[0])
                    callback()
                }
            })
        }
    })
}

var bufferResourceData = (callback) => {
    var options = {
        url: "https://api.openchargemap.io/v2/referencedata/",
        method: 'GET'
    }
    request(options, (err, response, body) => {
        if (err) {
            callback(err)
        }
        else {
            bufferedResources = JSON.parse(body)
            /*for (var i in bufferedResources) {
                console.log(i)
            }*/
            var values = []
            var keys = []
            for (var i in bufferedResources.ConnectionTypes) {
                fs.appendFileSync('values.txt', "<item>" + bufferedResources.ConnectionTypes[i].Title + "</item>\r\n");
                fs.appendFileSync('keys.txt', "<item>" + bufferedResources.ConnectionTypes[i].ID + "</item>\r\n");
                //values.push("<item>" + bufferedResources.ConnectionTypes[i].Title + "</item>\n");
                //keys.push("<item>" + bufferedResources.ConnectionTypes[i].ID + "</item>\n");
            }
            callback()
        }
    })
}
async.series(
    [
        bufferPointData,
        bufferResourceData
    ],
    (err) => {
        if (err) {
            console.log(err)
        }
        else {
            startServer();
        }
    }
)