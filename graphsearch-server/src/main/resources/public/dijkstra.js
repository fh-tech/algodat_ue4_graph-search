$(document).ready(function () {
    // add autocomplete on keyup for the two inputs
   $("#station_to").on('keyup', function (e) {
       feedAwesomeplete(e.target);
   });
    $("#station_from").on('keyup', function (e) {
        feedAwesomeplete(e.target);
    });

    // prevent page reload and handle call to api
    $("#dijkstra_form").submit(async function(e) {
        // prevent the reload
        e.preventDefault();
        let from = e.target.elements["station_from"].value;
        let to = e.target.elements["station_to"].value;
        let path;
        try {
            path = await find_shortest(from, to);
            showPath(path);
        } catch(e) {
            showErrorMessage(path.message);
        }
    })
});

async function feedAwesomeplete(target) {

    let stations = await fetchStations(target.value);

    let dataList = document.getElementById(target.getAttribute("list"));
    while (dataList.firstChild) {
        dataList.removeChild(dataList.firstChild);
    }

    for(let i = 0; i < 10 && i < stations.length; i++) {
        let option = document.createElement("option");
        option.setAttribute("value", stations[i]);
        dataList.appendChild(option);
    }
}

async function fetchStations(station) {
    let response = await fetch(`api/graph/name?station=${station}`);
    let json = await response.json();
    return json.map(obj => {return obj.stationName});
}

async function find_shortest(from, to) {
    let path = await fetch(`api/graph/path?from=${from}&to=${to}`);
    return await path.json();

}

function showPath(path) {
    let container = $("#result");
    let lastLine = null;
    let sumTime = 0;

    // clean current content
    cleanResult();
    for(let segment of path) {
        addStationCol(segment.prev.stationName, container);

        let line = segment.line.name;
        addLineDurCol(segment.duration, line, lastLine, container);
        sumTime += (lastLine != null && line !== lastLine) ? segment.duration + 5 : segment.duration;
        lastLine = line;
    }
    // add last station at end
    addStationCol(path[path.length - 1].next.stationName, container);
    // add sum time
    let col = $("<div>", {"class": "col-12 my-2"});
    let time = $("<span>", {"class": "font-weight-bold"}).text("Total time: " + sumTime + "min");
    col.append(time);
    container.append(col);
}

function addStationCol(fromStation, container) {
    let col = $("<div>", {"class": "col-12 station_segment my-2"});
    let contain = $("<div>", {"class": "path_contain text-center"});
    let from = $("<span>", {"class": "font-weight-bold"}).text(fromStation);
    contain.append(from);
    col.append(contain);
    container.append(col);
}

function addLineDurCol(duration, line, lastLine, container) {
    if(lastLine !== null && lastLine !== line) {
        let col = $("<div>", {"class": "col-12 dur_segment my-2"});
        let penalty =  $("<span>", {"class": "switch_penalty"}).text(lastLine + "-->" + line + " (5min)");
        let contain = $("<div>", {"class": "path_contain text-center"});
        contain.append(penalty);
        col.append(contain);
        container.append(col);
    }
    let col = $("<div>", {"class": "col-12 dur_segment my-2"});
    let contain = $("<div>", {"class": "path_contain text-center"});
    let line_dur = $("<span>").text("Line: " + line + "(" + duration +"min)");
    contain.append(line_dur);
    col.append(contain);
    container.append(col);
}

function showErrorMessage(message) {
    // cleans current contents
    cleanResult();
    let container = $("#result");
    let col = $("<div>", {"class": "col-12 my-2"});
    let contain = $("<div>", {"class": "path_contain text-center"});
    let fail = $("<span>", {"class": "font-weight-bold text-danger"}).text(message);
    contain.append(fail);
    col.append(contain);
    container.append(col);
}

//cleans current content of result
function cleanResult() {
    // TODO: would be interesting why i sometimes need [0] sometimes not
    let container = $("#result")[0];
    while (container.firstChild) {
        container.removeChild(container.firstChild);
    }
}