<?php
date_default_timezone_set("Asia/Kolkata");
if (isset($_GET["p"])) {
    //get headers
    $headers = apache_request_headers();
    $appVersion = (int) $headers['app_version'];

    if ($_GET["p"] == "getLatestItems") {
        getLatestItems();
    } else
    if ($_GET["p"] == "getMovies") {
        getMovies();
    } else
    if ($_GET["p"] == "getSearchedMovies") {
        getSearchedMovies();
    } else
    if ($_GET["p"] == "getMovieByID") {
        getMovieByID();
    } else
    if ($_GET["p"] == "getCasts") {
        getCasts();
    } else
    if ($_GET["p"] == "getWebseries") {
        getWebseries();
    } else
    if ($_GET["p"] == "getSearchedWebseries") {
        getSearchedWebseries();
    } else
    if ($_GET["p"] == "getWebseriesByID") {
        getWebseriesByID();
    } else
    if ($_GET["p"] == "addCast") {
        addCast();
    } else
    if ($_GET["p"] == "addMovie") {
        addMovie();
    } else
    if ($_GET["p"] == "addWebseries") {
        addWebseries();
    }
}

function getLatestItems()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Limit is 25 that means we will show 25 items at once
        $limit = 7;

        mysqli_set_charset($conn, 'utf8');

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        $query = mysqli_query($conn, "SELECT id, category, title, poster, imdb_rating, certificate, added_at from movies where status='PUBLISHED' order by id DESC LIMIT $limit");

        while ($row = mysqli_fetch_array($query)) {

            $array_out[] =
                array(
                    "id" => $row['id'],
                    "title" => $row['title'],
                    "category" => $row['category'],
                    "poster" => $row['poster'],
                    "imdb_rating" => $row['imdb_rating'],
                    "certificate" => $row['certificate'],
                    "added_at" => $row['added_at']
                );
        }
        $output = array("code" => "200", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}

function getMovies()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Getting the page number which is to be displayed
        $page = $_GET['page'];

        $category = $_GET['category'];

        //Initially we show the data from 1st row that means the 0th row
        $start = 0;

        //Limit is 25 that means we will show 25 items at once
        $limit = 12;

        mysqli_set_charset($conn, 'utf8');

        //Counting the total item available in the database
        $total = mysqli_num_rows(mysqli_query($conn, "SELECT id, title, poster, imdb_rating, certificate, added_at from movies where status='PUBLISHED' and category='$category'"));

        //We can go atmost to page number total/limit
        $page_limit = ceil($total / $limit);

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        if ($page <= $page_limit) {

            //Calculating start for every given page number
            $start = ($page - 1) * $limit;

            $query = mysqli_query($conn, "SELECT id, title, poster, imdb_rating, certificate, added_at from movies where status='PUBLISHED' and category='$category' order by id DESC LIMIT $start, $limit");

            while ($row = mysqli_fetch_array($query)) {

                $array_out[] =
                    array(
                        "id" => $row['id'],
                        "title" => $row['title'],
                        "poster" => $row['poster'],
                        "imdb_rating" => $row['imdb_rating'],
                        "certificate" => $row['certificate'],
                        "added_at" => $row['added_at']
                    );
            }
            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out[] =
                array(
                    "response" => "No more items!",
                );

            $output = array("code" => "400", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    }
}

function getSearchedMovies()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Getting the page number which is to be displayed
        $page = $_GET['page'];

        $searched_string = $_GET['searched_string'];

        //Initially we show the data from 1st row that means the 0th row
        $start = 0;

        //Limit is 25 that means we will show 25 items at once
        $limit = 12;

        mysqli_set_charset($conn, 'utf8');

        //Counting the total item available in the database
        $total = mysqli_num_rows(mysqli_query($conn, "SELECT id, title, poster, imdb_rating, certificate, added_at from movies where status='PUBLISHED' and 
        (actors like '%$searched_string%' or directors like '%$searched_string%' or writers like '%$searched_string%' or genres like '%$searched_string%' or release_date like '%$searched_string%' or description like '%$searched_string%')"));

        //We can go atmost to page number total/limit
        $page_limit = ceil($total / $limit);

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        if ($page <= $page_limit) {

            //Calculating start for every given page number
            $start = ($page - 1) * $limit;

            $query = mysqli_query($conn, "SELECT id, title, poster, imdb_rating, certificate, added_at from movies where status='PUBLISHED' and 
            (actors like '%$searched_string%' or directors like '%$searched_string%' or writers like '%$searched_string%' or genres like '%$searched_string%' or release_date like '%$searched_string%' or description like '%$searched_string%') order by id DESC LIMIT $start, $limit");

            while ($row = mysqli_fetch_array($query)) {

                $array_out[] =
                    array(
                        "id" => $row['id'],
                        "title" => $row['title'],
                        "poster" => $row['poster'],
                        "imdb_rating" => $row['imdb_rating'],
                        "certificate" => $row['certificate'],
                        "added_at" => $row['added_at']
                    );
            }
            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out[] =
                array(
                    "response" => "No more items!",
                );

            $output = array("code" => "400", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    }
}

function getMovieByID()
{
    require_once('ConnectDB.php');
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);
    //print_r($event_json);

    if (isset($event_json['uid'])) {

        $movieID = $event_json['id'];

        $getMovieDetails = "select languages, release_date, actors, genres, directors, writers, duration, description from movies where id='" . $movieID . "' ";
        $movieResult = mysqli_query($conn, $getMovieDetails);
        $movieDetailsObject = mysqli_fetch_object($movieResult);

        $array_out = array();
        $array_out[] =
            array(
                "languages" => $movieDetailsObject->languages,
                "release_date" => $movieDetailsObject->release_date,
                "actors" => $movieDetailsObject->actors,
                "genres" => $movieDetailsObject->genres,
                "directors" => $movieDetailsObject->directors,
                "writers" => $movieDetailsObject->writers,
                "duration" => $movieDetailsObject->duration,
                "description" => $movieDetailsObject->description

            );

        $output = array("code" => "200", "msg" => $array_out);
        print_r(json_encode($output, true));
    } else {
        $array_out = array();

        $array_out[] =
            array(
                "response" => "MOVIES DETAILS: JSON PARAMETERS ARE MISSING"
            );

        $output = array("code" => "201", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}

function getCasts()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Getting the page number which is to be displayed
        $page = $_GET['page'];

        //Initially we show the data from 1st row that means the 0th row
        $start = 0;

        //Limit is 25 that means we will show 25 items at once
        $limit = 12;

        mysqli_set_charset($conn, 'utf8');

        //Counting the total item available in the database
        $total = mysqli_num_rows(mysqli_query($conn, "SELECT * from casts where status='PUBLISHED'"));

        //We can go atmost to page number total/limit
        $page_limit = ceil($total / $limit);

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        if ($page <= $page_limit) {

            //Calculating start for every given page number
            $start = ($page - 1) * $limit;

            $query = mysqli_query($conn, "SELECT * from casts where status='PUBLISHED' order by id DESC LIMIT $start, $limit");

            while ($row = mysqli_fetch_array($query)) {

                $array_out[] =
                    array(
                        "id" => $row['id'],
                        "name" => $row['name'],
                        "image" => $row['image'],
                        "added_at" => $row['added_at']
                    );
            }
            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out[] =
                array(
                    "response" => "No more items!",
                );

            $output = array("code" => "400", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    }
}

function getWebseries()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Getting the page number which is to be displayed
        $page = $_GET['page'];

        //Initially we show the data from 1st row that means the 0th row
        $start = 0;

        //Limit is 25 that means we will show 25 items at once
        $limit = 12;

        mysqli_set_charset($conn, 'utf8');

        //Counting the total item available in the database
        $total = mysqli_num_rows(mysqli_query($conn, "SELECT * from webseries where status='PUBLISHED'"));

        //We can go atmost to page number total/limit
        $page_limit = ceil($total / $limit);

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        if ($page <= $page_limit) {

            //Calculating start for every given page number
            $start = ($page - 1) * $limit;

            $query = mysqli_query($conn, "SELECT id, title, main_thumbnail, season_tag, imdb_rating, certificate, added_at from webseries where status='PUBLISHED' order by id DESC LIMIT $start, $limit");

            while ($row = mysqli_fetch_array($query)) {

                $array_out[] =
                    array(
                        "id" => $row['id'],
                        "title" => $row['title'],
                        "main_thumbnail" => $row['main_thumbnail'],
                        "season_tag" => $row['season_tag'],
                        "imdb_rating" => $row['imdb_rating'],
                        "certificate" => $row['certificate'],
                        "added_at" => $row['added_at']
                    );
            }
            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out[] =
                array(
                    "response" => "No more items!",
                );

            $output = array("code" => "400", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    }
}

function getSearchedWebseries()
{

    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    if ($event_json['uid'] != "") {

        //Getting the page number which is to be displayed
        $page = $_GET['page'];

        $searched_string = $_GET['searched_string'];

        //Initially we show the data from 1st row that means the 0th row
        $start = 0;

        //Limit is 25 that means we will show 25 items at once
        $limit = 12;

        mysqli_set_charset($conn, 'utf8');

        //Counting the total item available in the database
        $total = mysqli_num_rows(mysqli_query($conn, "SELECT * from webseries where status='PUBLISHED' and 
        (actors like '%$searched_string%' or creators like '%$searched_string%' or original_network like '%$searched_string%' or genres like '%$searched_string%' or release_date like '%$searched_string%' or description like '%$searched_string%')"));

        //We can go atmost to page number total/limit
        $page_limit = ceil($total / $limit);

        $array_out = array();
        //If the page number is more than the limit we cannot show anything
        if ($page <= $page_limit) {

            //Calculating start for every given page number
            $start = ($page - 1) * $limit;

            $query = mysqli_query($conn, "SELECT id, title, main_thumbnail, season_tag, imdb_rating, certificate, added_at from webseries where status='PUBLISHED' and 
            (actors like '%$searched_string%' or creators like '%$searched_string%' or original_network like '%$searched_string%' or genres like '%$searched_string%' or release_date like '%$searched_string%' or description like '%$searched_string%') order by id DESC LIMIT $start, $limit");

            while ($row = mysqli_fetch_array($query)) {

                $array_out[] =
                    array(
                        "id" => $row['id'],
                        "title" => $row['title'],
                        "main_thumbnail" => $row['main_thumbnail'],
                        "season_tag" => $row['season_tag'],
                        "imdb_rating" => $row['imdb_rating'],
                        "certificate" => $row['certificate'],
                        "added_at" => $row['added_at']
                    );
            }
            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out[] =
                array(
                    "response" => "No more items!",
                );

            $output = array("code" => "400", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    }
}

function getWebseriesByID()
{
    require_once('ConnectDB.php');
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);
    //print_r($event_json);

    if (isset($event_json['uid'])) {

        $webseriesID = $event_json['id'];

        $getWebseriesDetails = "select languages, release_date, actors, genres, creators, original_network, duration, description, total_season, total_episode from webseries where id='" . $webseriesID . "' ";
        $webseriesResult = mysqli_query($conn, $getWebseriesDetails);
        $webseriesDetailsObject = mysqli_fetch_object($webseriesResult);

        $array_out = array();
        $array_out[] =
            array(
                "languages" => $webseriesDetailsObject->languages,
                "release_date" => $webseriesDetailsObject->release_date,
                "actors" => $webseriesDetailsObject->actors,
                "genres" => $webseriesDetailsObject->genres,
                "creators" => $webseriesDetailsObject->creators,
                "original_network" => $webseriesDetailsObject->original_network,
                "duration" => $webseriesDetailsObject->duration,
                "description" => $webseriesDetailsObject->description,
                "total_season" => $webseriesDetailsObject->total_season,
                "total_episode" => $webseriesDetailsObject->total_episode

            );

        $output = array("code" => "200", "msg" => $array_out);
        print_r(json_encode($output, true));
    } else {
        $array_out = array();

        $array_out[] =
            array(
                "response" => "MOVIES DETAILS: JSON PARAMETERS ARE MISSING"
            );

        $output = array("code" => "201", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}

function addCast()
{
    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    mysqli_set_charset($conn, 'utf8');

    if (isset($event_json['name']) && isset($event_json['image'])) {

        $name = htmlspecialchars(strip_tags($event_json['name'], ENT_QUOTES));
        $image = $event_json['image'];

        $qrry_1 = "insert into casts(name, image, added_at, added_by, status)values(";
        $qrry_1 .= "'" . $name . "',";
        $qrry_1 .= "'" . $image . "',";
        $qrry_1 .= "'" . date("Y-m-d H:i:s") . "',";
        $qrry_1 .= "'" . 'SDEEPAKKUMAR' . "',";
        $qrry_1 .= "'" . 'PUBLISHED' . "'";
        $qrry_1 .= ")";


        if (mysqli_query($conn, $qrry_1)) {
            $array_out = array();
            $array_out[] =
                array(
                    "response" => "Cast Added Successfully!"
                );

            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out = array();

            $array_out[] =
                array(
                    "response" => "Failed To Add Cast"
                );

            $output = array("code" => "301", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    } else {
        $array_out = array();
        $array_out[] =
            array(
                "response" => "ADDING CAST: JSON PARAMETER ARE MISSING"
            );

        $output = array("code" => "301", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}

function addMovie()
{
    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    mysqli_set_charset($conn, 'utf8');

    if (isset($event_json['title']) && isset($event_json['poster'])) {

        $category = $event_json['category'];
        $title = $event_json['title'];
        $description = str_replace("'", "\'", $event_json['description']);
        $actors = $event_json['actors'];
        $directors = $event_json['directors'];
        $writers = $event_json['writers'];
        $imdb_rating = $event_json['imdb_rating'];
        $release_date = $event_json['release_date'];
        $genres = $event_json['genres'];
        $duration = $event_json['duration'];
        $certificate = $event_json['certificate'];
        $poster = $event_json['poster'];
        $languages = $event_json['languages'];

        $qrry_1 = "insert into movies(category, certificate, imdb_rating, languages, poster, release_date, status, title, added_at, added_by, actors, description, directors, duration, genres, writers)values(";
        $qrry_1 .= "'" . $category . "',";
        $qrry_1 .= "'" . $certificate . "',";
        $qrry_1 .= "'" . $imdb_rating . "',";
        $qrry_1 .= "'" . $languages . "',";
        $qrry_1 .= "'" . $poster . "',";
        $qrry_1 .= "'" . $release_date . "',";
        $qrry_1 .= "'" . 'PUBLISHED' . "',";
        $qrry_1 .= "'" . $title . "',";
        $qrry_1 .= "'" . date("Y-m-d H:i:s") . "',";
        $qrry_1 .= "'" . 'SDEEPAKKUMAR' . "',";
        $qrry_1 .= "'" . $actors . "',";
        $qrry_1 .= "'" . $description . "',";
        $qrry_1 .= "'" . $directors . "',";
        $qrry_1 .= "'" . $duration . "',";
        $qrry_1 .= "'" . $genres . "',";
        $qrry_1 .= "'" . $writers . "'";
        $qrry_1 .= ")";


        if (mysqli_query($conn, $qrry_1)) {
            $array_out = array();
            $array_out[] =
                array(
                    "response" => "Movie Added Successfully!"
                );

            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out = array();

            $array_out[] =
                array(
                    "response" => "Failed To Add Movie"
                );

            $output = array("code" => "301", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    } else {
        $array_out = array();
        $array_out[] =
            array(
                "response" => "ADDING MOVIE: JSON PARAMETER ARE MISSING"
            );

        $output = array("code" => "301", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}

function addWebseries()
{
    require_once("ConnectDB.php");
    $input = @file_get_contents("php://input");
    $event_json = json_decode($input, true);

    mysqli_set_charset($conn, 'utf8');

    if (isset($event_json['title']) && isset($event_json['main_thumbnail'])) {

        $season_tag = $event_json['season_tag'];
        $title = $event_json['title'];
        $description = str_replace("'", "\'", $event_json['description']);
        $actors = $event_json['actors'];
        $creators = $event_json['creators'];
        $original_network = $event_json['original_network'];
        $imdb_rating = $event_json['imdb_rating'];
        $release_date = $event_json['release_date'];
        $genres = $event_json['genres'];
        $duration = $event_json['duration'];
        $certificate = $event_json['certificate'];
        $main_thumbnail = $event_json['main_thumbnail'];
        $languages = $event_json['languages'];
        $total_season = $event_json['total_season'];
        $total_episode = $event_json['total_episode'];
        $season_poster = $event_json['season_poster'];

        $qrry_1 = "insert into webseries(season_tag, certificate, imdb_rating, languages, main_thumbnail, release_date, status, title, added_at, added_by, actors, description, creators, duration, genres, original_network, total_episode, total_season, season_poster)values(";
        $qrry_1 .= "'" . $season_tag . "',";
        $qrry_1 .= "'" . $certificate . "',";
        $qrry_1 .= "'" . $imdb_rating . "',";
        $qrry_1 .= "'" . $languages . "',";
        $qrry_1 .= "'" . $main_thumbnail . "',";
        $qrry_1 .= "'" . $release_date . "',";
        $qrry_1 .= "'" . 'PUBLISHED' . "',";
        $qrry_1 .= "'" . $title . "',";
        $qrry_1 .= "'" . date("Y-m-d H:i:s") . "',";
        $qrry_1 .= "'" . 'SDEEPAKKUMAR' . "',";
        $qrry_1 .= "'" . $actors . "',";
        $qrry_1 .= "'" . $description . "',";
        $qrry_1 .= "'" . $creators . "',";
        $qrry_1 .= "'" . $duration . "',";
        $qrry_1 .= "'" . $genres . "',";
        $qrry_1 .= "'" . $original_network . "',";
        $qrry_1 .= "'" . $total_episode . "',";
        $qrry_1 .= "'" . $total_season . "',";
        $qrry_1 .= "'" . $season_poster . "'";
        $qrry_1 .= ")";


        if (mysqli_query($conn, $qrry_1)) {
            $array_out = array();
            $array_out[] =
                array(
                    "response" => "Webseries Added Successfully!"
                );

            $output = array("code" => "200", "msg" => $array_out);
            print_r(json_encode($output, true));
        } else {
            $array_out = array();

            $array_out[] =
                array(
                    "response" => "Failed To Add Webseries"
                );

            $output = array("code" => "301", "msg" => $array_out);
            print_r(json_encode($output, true));
        }
    } else {
        $array_out = array();
        $array_out[] =
            array(
                "response" => "ADDING WEBSERIES: JSON PARAMETER ARE MISSING"
            );

        $output = array("code" => "301", "msg" => $array_out);
        print_r(json_encode($output, true));
    }
}
