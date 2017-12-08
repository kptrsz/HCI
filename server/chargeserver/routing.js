var geo = require('node-geo-distance');
var async = require('async');



var getBufferedPointsInDistance = (bufferedPoints, point, distance, callback) => {
    var points = []
    for (var i in bufferedPoints) {
        var bufferedPoint = bufferedPoints[i]
        var coord = {
            latitude: bufferedPoint.AddressInfo.Latitude,
            longitude: bufferedPoint.AddressInfo.Longitude
        }
        var dist = parseFloat(geo.vincentySync(coord, point));
        if (dist <= distance) {
            points.push(bufferedPoint)
        }
    }
    async.setImmediate(callback, null, points)
}

var bfs = function (bufferedPoints, params, callback) {
    /*var coord1 = {
        latitude: 38.8977330,
        longitude: -77.0365310
    }

    // Washington Monument 
    var coord2 = {
        latitude: 38.8894840,
        longitude: -77.0352790
    }
    geo.vincenty(coord1, coord2, function (dist) {
        console.log(dist);
    });
    var vincentyDist = geo.vincentySync(coord1, coord2);
    */


    var lastNodes = {}
    lastNodes['start'] = {
        id: "start",
        latitude: params.start.latitude,
        longitude: params.start.longitude,
        reachableFrom: []
    }
    var endNode = {
        id: "end",
        latitude: params.end.latitude,
        longitude: params.end.longitude,
        reachableFrom: []
    }
    var allNodes = {
        end: endNode
    }
    var idx = 0;
    async.whilst(
        () => {
            if (Object.keys(lastNodes).length == 0) {
                console.log("CANT REACH")
                return false;
            }
            for (var i in lastNodes) {
                var dist = parseFloat(geo.vincentySync(lastNodes[i], params.end))
                if (dist <= params.dist) {
                    // console.log(dist)
                    endNode.reachableFrom.push(lastNodes[i].id)
                }
            }
            if (endNode.reachableFrom.length > 0) {
                return false;
            }
            return true;
        },
        (callback) => {
            idx++;
            //console.log("IT: "+idx)
            var tmp = {}
            async.eachSeries(
                lastNodes,
                (node, callback) => {
                    getBufferedPointsInDistance(bufferedPoints, node, params.dist, (err, data) => {
                        if (err) { callback(err) }
                        else {
                            // console.log(data.length)
                            for (var i in data) {
                                if (tmp.hasOwnProperty(data[i].ID)) {
                                    tmp[data[i].ID].reachableFrom.push(node.id)
                                }
                                else {
                                    tmp[data[i].ID] = {
                                        id: data[i].ID,
                                        latitude: data[i].AddressInfo.Latitude,
                                        longitude: data[i].AddressInfo.Longitude,
                                        reachableFrom: [node.id]
                                    }
                                }
                            }
                            callback()
                        }
                    })
                    /*var par = {
                        maxresults: 100000,
                        latitude: node.latitude,
                        longitude: node.longitude,
                        distance: params.dist/1000,
                        distanceunit: "KM",
                        compact: true,
                        verbose:false
                    }
                    openChargeMapQuery(par, (err, result) => {
                        if (err) { callback(err) }
                        else {
                            var data = JSON.parse(result)
                            console.log(data.length)
                            for (var i in data) {
                               if (tmp.hasOwnProperty(data[i].ID)) {
                                    tmp[data[i].ID].reachableFrom.push(node.id)
                                }
                                else {
                                    tmp[data[i].ID] = {
                                        id: data[i].ID,
                                        latitude: data[i].AddressInfo.Latitude,
                                        longitude: data[i].AddressInfo.Longitude,
                                        reachableFrom: [node.id]
                                    }
                                }
                            }
                            callback()
                        }
                        
                    })*/
                },
                (err) => {
                    for (var i in lastNodes) {
                        allNodes[i] = lastNodes[i]
                    }
                    lastNodes = tmp;
                    async.setImmediate(callback, err)
                }
            )
        },
        (err) => {
            for (var i in lastNodes) {
                allNodes[i] = lastNodes[i]
            }
            dijkstra(allNodes, allNodes['start'], allNodes['end'], params.dist)
            var waypoints = [allNodes['end']]
            while (waypoints[waypoints.length - 1].id != "start") {
                waypoints.push(allNodes[waypoints[waypoints.length - 1].from])
            }
            waypoints = waypoints.reverse();
            async.setImmediate(callback, err, waypoints)
        }
    )
}

var dijkstra = (points, start, end, limit) => {
    start.dist = 0;
    var activepoints = [start]
    while (activepoints.indexOf(end) == -1) {
        var tmp = []
        for (var i in activepoints) {
            for (var j in points) {
                if (!points[j].closed) {
                    var dist = parseFloat(geo.vincentySync(points[j], activepoints[i]))
                    if (dist < limit) {
                        if (points[j].dist) {
                            if ((dist + activepoints[i].dist) < points[j].dist) {
                                points[j].dist = dist + activepoints[i].dist;
                                points[j].from = activepoints[i].id
                            }
                        }
                        else {
                            points[j].dist = dist + activepoints[i].dist;
                            points[j].from = activepoints[i].id
                            tmp.push(points[j])
                        }
                    }
                }
            }
            tmp.sort((a, b) => { return a.dist - b.dist })
            activepoints[i].closed = true;
        }
        activepoints = tmp;
    }
}

module.exports = {
    planRoute: bfs,
    getPointsInDistance: getBufferedPointsInDistance
};