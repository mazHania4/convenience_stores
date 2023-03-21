<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cargar entrada</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!--icon-->
    <link rel="icon" href="../images/icon.png" type="image/x-icon">
    <!-- CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body class>
    <div class="container-fluid py-2 bg-light">
        <div class="row justify-content-center">
            <div class="col-6 text-center">
                <h1 class="mt-2 ml-3"> </h1>
            </div>
        </div>
    </div>
    <div class="container-fluid p-3 mb-2 bg-dark text-white">
        <div class="row justify-content-center">
            <div class="col-8 text-center">
                <h5 class="mt-2 ml-3">La base de datos aún no ha sido iniciada</h5>
                <h1 class="mt-2 ml-3 text-center">Carga de archivo de entrada</h1>
            </div>
        </div>
    </div>

    <div class="container-fluid py-2 bg-light">
        <div class="row justify-content-center">
            <div class="col-6 text-center">
                <h1 class="mt-2 ml-3"> </h1>
            </div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/entryFile-servlet" method="POST" enctype="multipart/form-data">
        <div class="containter-fluid">
            <div class="row pl-5 justify-content-center">
                <div class="col-md-6">
                    <div>
                        <label for="entryFile" class="form-label">Seleccione el archivo de entrada:</label>
                        <input class="form-control border" name="entryFile" type="file" accept=".json">
                    </div>
                </div>
            </div>
            <div class="row pl-5 justify-content-end">
                <div class="col-md-4 align-self-end">
                    <button type="submit" class="btn btn-primary btn-block mt-2">Continuar</button>
                </div>
            </div>
        </div>
    </form>

    <!-- JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>
