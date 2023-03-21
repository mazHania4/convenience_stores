<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Editar usuario supervisor</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- ** CSS Plugins Needed for the Project ** -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/bootstrap/bootstrap.min.css">
    <!-- themefy-icon -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/themify-icons/themify-icons.css">
    <!--icon-->
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/x-icon">
    <!-- fonts -->
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700&display=swap" rel="stylesheet">
    <!-- Main Stylesheet -->
    <link href="${pageContext.request.contextPath}/assets/style.css" rel="stylesheet" media="screen" />
</head>
<body>
<!-- header -->
<header class="shadow-bottom sticky-top bg-info.bg-gradient">
    <!-- navBar -->
    <jsp:include page="../WEB-INF/navBar.jsp"/>
    <!-- /navBar -->
</header>
<!-- /header -->

<!-- manage user -->
<section class="section pb-0">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2 class="section-title text-primary">Crear o editar usuarios supervisores</h2>

                <!-- Show error if there is -->
                <c:if test="${!empty(error)}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>No se pudo completar la acción</strong> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Show success if there is -->
                <c:if test="${!empty(success)}">
                    <div class="alert alert-success d-flex align-items-center" role="alert">
                        <strong>Acción completada</strong> ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <!-- accordion -->
                <div id="accordion">
                    <div class="card mb-4 rounded-0 shadow border-0">
                        <div class="card-header rounded-0 bg-white border p-0 border-0">
                            <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                               href="#create-user">
                                <span>Crear Usuario</span> <i class="ti-plus text-primary text-right"></i>
                            </a>
                        </div>
                        <div id="create-user" class="collapse" data-parent="#accordion">
                            <div class="card-body font-secondary text-color">
                                <form action="${pageContext.request.contextPath}/manage-user-servlet" method="POST">
                                    <!-- hidden UserType input -->
                                    <input type="hidden" id="newUserType" name="newUserType" value="supervisor_user">
                                    <!-- Name input -->
                                    <input type="text" name="newName" id="newName" placeholder="Nombre" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- Username input -->
                                    <input type="text" name="newUsername" id="newUsername" placeholder="Username" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- email input -->
                                    <input type="email" name="newEmail" id="newEmail" placeholder="email" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- Password input -->
                                    <input type="password" name="newPassword" id="newPassword" placeholder="Contraseña" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- hidden identifier -->
                                    <input type="hidden" name="create" value="true">
                                    <!-- Submit button -->
                                    <button type="submit" class="btn btn-primary btn-block mb-4">Crear</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="card mb-4 rounded-0 shadow border-0">
                        <div class="card-header rounded-0 bg-white border p-0 border-0">
                            <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                               href="#edit-user">
                                <span>Editar usuario</span> <i
                                    class="ti-plus text-primary text-right"></i>
                            </a>
                        </div>
                        <div id="edit-user" class="collapse" data-parent="#accordion">
                            <div class="card-body font-secondary text-color">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Username</th>
                                        <th>Email</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${supervisorUsers}" var="supUser">
                                        <tr>
                                            <td><strong>${supUser.id}</strong></td>
                                            <td><em>${supUser.name}</em></td>
                                            <td><em>${supUser.username}</em></td>
                                            <td><em>${supUser.email}</em></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <form action="${pageContext.request.contextPath}/manage-user-servlet" method="POST">
                                    <!-- hidden UserType input -->
                                    <input type="hidden" id="userType" name="userType" value="supervisor_user">
                                    <!-- UserId input -->
                                    <input type="text" name="userId" id="userId" placeholder="Id del usuario a modificar" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- Name input -->
                                    <input type="text" name="name" id="name" placeholder="Nombre" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- Username input -->
                                    <input type="text" name="username" id="username" placeholder="Username" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- email input -->
                                    <input type="email" name="email" id="email" placeholder="email" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- Password input -->
                                    <input type="password" name="password" id="password" placeholder="Contraseña" class="form-control mb-4 shadow rounded-0"/>
                                    <!-- hidden identifier -->
                                    <input type="hidden" name="update" value="true">
                                    <!-- Submit button -->
                                    <button type="submit" class="btn btn-primary btn-block mb-4">Modificar</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- /manage user -->

<!-- footer -->
<jsp:include page="../WEB-INF/footer.jsp"/>
<!-- /footer -->

<!-- ** JS Plugins Needed for the Project ** -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
<!-- jquiry -->
<script src="${pageContext.request.contextPath}/plugins/jquery/jquery-1.12.4.js"></script>
<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/plugins/bootstrap/bootstrap.min.js"></script>
<!-- match-height JS -->
<script src="${pageContext.request.contextPath}/plugins/match-height/jquery.matchHeight-min.js"></script>
<!-- Main Script -->
<script src="${pageContext.request.contextPath}/assets/script.js"></script>
</body>
</html>
