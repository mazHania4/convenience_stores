<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log In</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!--icon-->
  <link rel="icon" href="../images/icon.png" type="image/x-icon">
  <!-- CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>

<!-- Section: Design Block -->
<section class="text-center">
  <!-- Background image -->
  <div class="p-5 bg-image" style="
        background-image: url('images/bglogin.jpg');
        height: 250px;
        "></div>

  <div class="card mx-4 mx-md-5 shadow-5-strong" style="
        margin-top: -100px;
        background: hsla(0, 0%, 100%, 0.8);
        backdrop-filter: blur(30px);
        ">
    <div class="card-body py-5 px-md-5">

      <div class="row d-flex justify-content-center">
        <div class="col-lg-8">
          <h1 class="fw-bold mb-5">Inicia sesión</h1>
          <form action="${pageContext.request.contextPath}/login-servlet" method="POST">

            <!-- Show error if there is -->
            <c:if test="${!empty(error)}">
              <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Login fallido!</strong> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>

            <!-- UserType input -->
            <div class="form-outline mb-4">
              <select id="userType" name="userType" class="form-control">
                <option value="store_user">Usuario de tienda</option>
                <option value="supervisor_user">Usuario supervisor</option>
                <option value="warehouse_user">Usuario de bodega central</option>
                <option value="admin_user">Usuario administrador</option>
              </select>
              <label for="userType">  Tipo de usuario</label><br>
            </div>

            <!-- Username input -->
            <div class="form-outline mb-4">
              <input type="text" name="username" id="username" class="form-control" />
              <label class="form-label" for="username">Nombre de usuario</label>
            </div>

            <!-- Password input -->
            <div class="form-outline mb-4">
              <input type="password" name="password" id="password" class="form-control" />
              <label class="form-label" for="password">Contraseña</label>
            </div>

            <!-- Submit button -->
            <button type="submit" class="btn btn-primary btn-block mb-4">
              Iniciar sesión
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</section>

<!-- JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>
