$(document).ready(function () {
   $("#station_to").on('keyup', function (e) {
       feedAwesomeplete(e.target);
   });
    $("#station_from").on('keyup', function (e) {
        feedAwesomeplete(e.target);
    });
});

async function feedAwesomeplete(target) {

    let stations = await fetchStations(target.value);

    let dataList = document.getElementById(target.getAttribute("list"));
    while (dataList.firstChild) {
        dataList.removeChild(dataList.firstChild);
    }

    for(let i = 0; i < 10 && i < stations.length; i++) {
        console.log("got here");
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
