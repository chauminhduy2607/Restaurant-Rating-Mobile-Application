var db = window.openDatabase("Restaurant", "1.0", "Restaurant", 200000);

if (navigator.userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry)/)) {
    $(document).on("deviceready", onDeviceReady);
} else {
    onDeviceReady();
}

function onDeviceReady() {
    db.transaction(function(tx) {
        var query = `CREATE TABLE IF NOT EXISTS Restaurant (Id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                            Name TEXT NOT NULL,
                                                            Address TEXT NOT NULL,
                                                            Type TEXT NOT NULL,
                                                            Visit DATE NOT NULL,
                                                            Price FLOAT NOT NULL,
                                                            Service FLOAT NOT NULL,
                                                            Clean FLOAT NOT NULL,
                                                            Food FLOAT NOT NULL,
                                                            Reporter TEXT NOT NULL
                                                            )`;

        tx.executeSql(query, [], function() {
            console.log(`Create TABLE Restaurant successfully!`);
        }, transError);

        query = `CREATE TABLE IF NOT EXISTS Note (Id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                  Note TEXT NOT NULL,
                                                  Restaurant_Id INTEGER NOT NULL,
                                                  FOREIGN KEY (Restaurant_Id) REFERENCES Retaurant (Id))`;
        tx.executeSql(query, [], function() {
            console.log("Create TABLE Note successfully!");
        }, transError);
    });
}

function transError(err) { alert("Error processing SQL: " + err.code); }

function insertRestaurant(restaurant) {
    db.transaction(function(tx) {
        var query = `INSERT INTO Restaurant (Name, Address, Type, Price, Visit, Service, Clean, Food, Reporter) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;

        tx.executeSql(query, [restaurant.Name, restaurant.Address, restaurant.Type,
                              restaurant.Price, restaurant.Visit, restaurant.Service,
                              restaurant.Clean, restaurant.Food, restaurant.Reporter], function() {
            console.log(`Create a new restaurant successfully!`);
        }, transError);
    });
}

function updateRestaurant(restaurant) {
    db.transaction(function(tx) {
        var query = `UPDATE Restaurant SET Name=?, Address=?, Type=?, Price=?, Visit=?, Service=?, Clean=?, Food=?, Reporter=? WHERE Id=?`;

        tx.executeSql(query, [restaurant.Name, restaurant.Address, restaurant.Type,
                              restaurant.Price, restaurant.Visit, restaurant.Service,
                              restaurant.Clean, restaurant.Food, restaurant.Reporter, restaurant.Id], function() {
            console.log(`Update restaurant successfully!`);
        }, transError);
    });
}

$(document).on("pageshow", "#page-list", selectRestaurant);

function selectRestaurant() {
    db.transaction(function(tx) {
        var query = `SELECT * FROM Restaurant`;

        tx.executeSql(query, [], function(tx, results) {
          $("#page-list #list").empty();

          var list = results.rows;

            var newList ="<ul data-role='listview' id='list' data-inset='true'>";

            if(list.length > 0){
                for(item of list){
                  console.log(item);
                  newList += `<li data-icon='false'>
                              <a href="#page-detail" class='ui-btn' data-details='${JSON.stringify(item)}'>
                                  <p><strong>${item.Name}</strong></p>
                                  <p><em>Address: ${item.Address}</em></p>
                                  <p><em>Rating: ${calRating(item.Service, item.Clean, item.Food)}</em></p>
                              </a></li>`;
                }
            } else{
              newList += "<li data-icon='false'><a href='#'><p>No Restaurant...</p></a></li>";
            }

            newList += "</ul>";

          $("#page-list #list").append(newList).trigger("create");
        }, transError);
    });
}

function deleteRestaurant(Id) {
    db.transaction(function(tx) {
        var query = `DELETE FROM Restaurant WHERE Id=${Id}`;

        tx.executeSql(query, [], function() {
            console.log(`Delete restaurant successfully!`);
        }, transError);
    });
}

function insertNote(note) {
    db.transaction(function(tx) {
        var query = `INSERT INTO Note (Note, Restaurant_Id) VALUES (?, ?)`;

        tx.executeSql(query, [note.Note, note.Restaurant_Id], function() {
            console.log(`Create a new note successfully!`);
        }, transError);
    });
}

function selectNote(Id) {
    db.transaction(function(tx) {
        var query = `SELECT Note FROM Note WHERE Restaurant_Id=${Id}`;

        tx.executeSql(query, [], function() {
            console.log(`select a new note successfully!`);
        }, transError);
    });
}

//onkeyup search restaurant
function searchRestaurant() {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById("search-name");
    filter = input.value.toUpperCase();
    ul = document.getElementById("list");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}

//change value select box to string
function ratingName(a){
  var text;
  switch (a){
    case 1:
      text = "Need to improve";
      break;
    case 2:
      text = "Bad";
      break;
    case 3:
      text = "Normal";
      break;
    case 4:
      text = "Good";
      break;
    default:
      text = "Good";
  }
  return text;
}

function calRating(s, c, f){
  var total_rating = (s + c + f)/3;
  return total_rating.toFixed(2);
}

$(document).on("submit", "#frm-create", confirmRestaurant);

function confirmRestaurant(e) {
    e.preventDefault();

    $("#page-create #frm-confirm #name"). text($("#page-create #frm-create #name").val());
    $("#page-create #frm-confirm #address"). text($("#page-create #frm-create #address").val());
    $("#page-create #frm-confirm #type"). text($("#page-create #frm-create #type").val());
    $("#page-create #frm-confirm #visit"). text($("#page-create #frm-create #visit").val());
    $("#page-create #frm-confirm #price"). text($("#page-create #frm-create #price").val());
    $("#page-create #frm-confirm #service"). text($("#page-create #frm-create #service").val());
    $("#page-create #frm-confirm #clean"). text($("#page-create #frm-create #clean").val());
    $("#page-create #frm-confirm #food"). text($("#page-create #frm-create #food").val());
    $("#page-create #frm-confirm #reporter"). text($("#page-create #frm-create #reporter").val());

    $("#page-create #frm-confirm").popup("open");
}

$(document).on("vclick", "#frm-confirm #btn-edit", function(){

  $("#page-create #frm-confirm").popup("close");
});

$(document).on("submit", "#frm-confirm", createRestaurant);

function createRestaurant(e){
  e.preventDefault();
  var restaurant = {
      "Name" : $("#page-create #frm-confirm #name").text(),
      "Address" : $("#page-create #frm-confirm #address").text(),
      "Type" : $("#page-create #frm-confirm #type").text(),
      "Visit" : $("#page-create #frm-confirm #visit").text(),
      "Price" : parseFloat($("#page-create #frm-confirm #price").text()),
      "Service" : parseInt($("#page-create #frm-confirm #service").text()),
      "Clean" : parseInt($("#page-create #frm-confirm #clean").text()),
      "Food" : parseInt($("#page-create #frm-confirm #food").text()),
      "Reporter" : $("#page-create #frm-confirm #reporter").text()
  };

  insertRestaurant(restaurant);
  $("#page-create #frm-confirm").popup("close");
  $("#page-create #frm-create").trigger("reset");
}

$(document).on("vclick","#page-list #list li a", viewDetail);

function viewDetail() {
  $("#page-detail #total-rating").remove();

  var restaurant = $(this).data("details");
  $("#page-detail #id").text(restaurant.Id);
  $("#page-detail #name").val(restaurant.Name);
  $("#page-detail #address").val(restaurant.Address);
  $("#page-detail #type").val(restaurant.Type);
  $("#page-detail #visit").val(restaurant.Visit);
  $("#page-detail #price").val(restaurant.Price);
  $("#page-detail #reporter").val(restaurant.Reporter);
  $("#page-detail #total-rating").remove();
  $("#page-detail #reporter").after(`<h2 id="total-rating">Total Rating: ${calRating(parseFloat(restaurant.Food), parseFloat(restaurant.Service), parseFloat(restaurant.Clean))}</h2><hr>`);

  $("#page-detail #name").attr("readonly", true);
  $("#page-detail #address").attr("readonly", true);
  $("#page-detail #type").attr("readonly", true);
  $("#page-detail #visit").attr("readonly", true);
  $("#page-detail #price").attr("readonly", true);
  $("#page-detail #reporter").attr("readonly", true);



  $("#page-detail #service").empty();
  $("#page-detail #clean").empty();
  $("#page-detail #food").empty();
  $("#page-detail #service").append(`<option value='${restaurant.Service}'>${ratingName(restaurant.Service)}</option>`).selectmenu().selectmenu('refresh',true);

  $("#page-detail #clean").append(`<option value='${restaurant.Clean}'>${ratingName(restaurant.Clean)}</option>`).selectmenu().selectmenu('refresh',true);

  $("#page-detail #food").append(`<option value='${restaurant.Food}'>${ratingName(restaurant.Food)}</option>`).selectmenu().selectmenu('refresh',true);
  $("#page-detail #btn-add-notes").append("<a href='#frm-add-note' data-rel='popup' data-position-to='window' data-transition='pop' class='ui-btn ui-corner-all ui-icon-plus ui-btn-inline ui-btn-icon-notext ui-nodisc-icon ui-alt-icon' data-details='" + restaurant.Id + "'>Add note</a>");
  viewNote(restaurant.Id);
}


function viewNote(id) {
    $("#page-detail #note").empty();

    db.transaction(function(tx) {
        var query = "SELECT * FROM Note WHERE Restaurant_Id = ?";

        tx.executeSql(query, [id], function(tx, result) {
            if(result.rows.length) {
                $.each(result.rows, function(i, noteItem) {
                    $("#page-detail #note").append("<p>" + noteItem.Note + "</p>");
                });
            }

            else {
                $("#page-detail #note").append("<p>There is no Note.</p>");
            }
        }, transError);
    });
}

$(document).on("vclick", "#page-detail #btn-add-notes a", function() {
    var id = $(this).data("details");

    $("#frm-add-note #txt-Id").val(id);
});

$(document).on("submit", "#frm-add-note", function(e) {
    e.preventDefault();

    var id = $("#frm-add-note #txt-Id").val();
    var note = $("#frm-add-note #ta-note").val();

    if(note != "") {
        db.transaction(function(tx) {
            var query = "INSERT INTO Note (Note, Restaurant_Id) VALUES (?, ?)";

            tx.executeSql(query, [note, id], function() {
                alert("Create a note successfully!");
                $("#frm-add-note #ta-note").val("");

                viewNote(id);
            }, transError);
        });
    }

    $.mobile.changePage("#page-detail", { transition: "none" });
});

$(document).on("vclick", "#frm-add-note #btn-clear", function() {
    $("#frm-add-note #ta-note").val("");
});


$(document).on("submit",'#page-detail #frm-detail',editInfo);

function editInfo(e) {

  e.preventDefault();

  if ("Edit" == $('#page-detail #frm-detail #btn-edit').html()) {

    $('#page-detail #name').attr("readonly", false);
    $('#page-detail #address').attr("readonly", false);
    $('#page-detail #type').attr("readonly", false);
    $('#page-detail #visit').attr("readonly", false);
    $('#page-detail #price').attr("readonly", false);
    $('#page-detail #reporter').attr("readonly", false);

    $('#page-detail #frm-detail #btn-edit').html("Update");
    $('#page-detail #frm-detail #btn-delete').attr("disabled",true);

    $("#page-detail #total-rating").remove();
    $("#page-detail #btn-add-notes").empty();
    //Save the previous value select box
    var item = {
      "Service" : $('#page-detail #service').val(),
      "Clean" : $('#page-detail #clean').val(),
      "Food" : $('#page-detail #food').val()
    }

    $("#page-detail #service").empty();
    $("#page-detail #clean").empty();
    $("#page-detail #food").empty();

    $('#page-detail #service').empty().append(`<option value="${item.Service}" disabled>${ratingName(parseInt(item.Service))}</option>
                                              <option value="1">Not Recommend</option>
                                              <option value="2">Bad</option>
                                              <option value="3">Normal</option>
                                              <option value="4">Good</option>
                                             `);

    $('#page-detail #clean').empty().append(`<option value="${item.Clean}" disabled>${ratingName(parseInt(item.Clean))}</option>
                                              <option value="1">Not Recommend</option>
                                              <option value="2">Bad</option>
                                              <option value="3">Normal</option>
                                              <option value="4">Good</option>
                                              `);

    $('#page-detail #food').empty().append(`<option value="${item.Food}" disabled>${ratingName(parseInt(item.Food))}</option>
                                              <option value="1">Not Recommend</option>
                                              <option value="2">Bad</option>
                                              <option value="3">Normal</option>
                                              <option value="4">Good</option>
                                              `);
  }

  else if ("Update" == $('#page-detail #frm-detail #btn-edit').html()) {
    var restaurant = {
        "Id" : $("#page-detail #frm-detail #id").text(),
        "Name" : $("#page-detail #frm-detail #name").val(),
        "Address" : $("#page-detail #frm-detail #address").val(),
        "Type" : $("#page-detail #frm-detail #type").val(),
        "Visit" : $("#page-detail #frm-detail #visit").val(),
        "Price" : parseFloat($("#page-detail #frm-detail #price").val()),
        "Service" : parseInt($("#page-detail #frm-detail #service").val()),
        "Clean" : parseInt($("#page-detail #frm-detail #clean").val()),
        "Food" : parseInt($("#page-detail #frm-detail #food").val()),
        "Reporter" : $("#page-detail #frm-detail #reporter").val()
    };

    updateRestaurant(restaurant);
    $('#page-detail #frm-detail #btn-edit').html("Edit");
    $('#page-detail #frm-detail #btn-delete').attr("disabled",false);
    $.mobile.navigate('#page-list');
  }
}

$(document).on("vclick", "#page-detail #frm-detail #btn-delete", removeRestaurant);

function removeRestaurant(){

  var restaurant = $("#page-detail #frm-detail #id").text();
  $.mobile.navigate('#page-list');
  deleteRestaurant(restaurant);
}
