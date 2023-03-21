
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Módulo administración</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- ** CSS Plugins Needed for the Project ** -->
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
    <header class="banner overlay bg-cover" data-background="${pageContext.request.contextPath}/images/adminbanner.jpg">
        <!-- navBar -->
        <jsp:include page="../WEB-INF/navBar.jsp"/>
        <!-- /navBar -->
        <!-- banner -->
        <div class="container section">
            <div class="row">
                <div class="col-lg-8 text-center mx-auto">
                    <h1 class="text-white mb-3">Módulo administración</h1>
                </div>
            </div>
        </div>
        <!-- /banner -->
    </header>
    <!-- /header -->


    <!-- options -->
    <section class="section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 text-center">
                    <h2 class="section-title">Gestionar usuarios</h2>
                </div>
                <div class="col-lg-4 col-sm-6 mb-4">
                    <a href="${pageContext.request.contextPath}/adminModule/editStoreUser.jsp" class="px-4 py-5 bg-white shadow text-center d-block match-height">
                        <i class="ti-shopping-cart-full icon text-primary d-block mb-4"></i>
                        <h3 class="mb-3 mt-0">Usuarios de tienda</h3>
                        <p class="mb-0">Crear o editar usuarios de tienda</p>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6 mb-4">
                    <a href="${pageContext.request.contextPath}/adminModule/editWarehouseUser.jsp" class="px-4 py-5 bg-white shadow text-center d-block match-height">
                        <i class="ti-package icon text-primary d-block mb-4"></i>
                        <h3 class="mb-3 mt-0">Usuarios de bodega central</h3>
                        <p class="mb-0">Crear o editar usuarios de bodega central</p>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6 mb-4">
                    <a href="${pageContext.request.contextPath}/adminModule/editSupervisorUser.jsp" class="px-4 py-5 bg-white shadow text-center d-block match-height">
                        <i class="ti-panel icon text-primary d-block mb-4"></i>
                        <h3 class="mb-3 mt-0">Usuarios supervisores</h3>
                        <p class="mb-0">Crear o editar usuarios supervisores</p>
                    </a>
                </div>
            </div>
        </div>
    </section>
    <!-- /options -->

    <!-- reports -->
    <section>
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="section px-3 bg-white shadow text-center">
                        <!-- accordion -->
                        <div id="accordion">

                            <div class="card mb-4 rounded-0 shadow border-0">
                                <div class="card-header rounded-0 bg-white border p-0 border-0">
                                    <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                                       href="#report1">
                                        <span>5 tiendas con más pedidos en un intervalo</span> <i class="ti-plus text-primary text-right"></i>
                                    </a>
                                </div>
                                <div id="report1" class="collapse" data-parent="#accordion">
                                    <div class="card-body font-secondary text-color">
                                        <form action="#" method="POST">
                                            <!-- initialDate input -->
                                            <input type="datetime-local" name="initialDate1" id="initialDate1" placeholder="Fecha inicial" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- finalDate input -->
                                            <input type="datetime-local" name="finalDate1" id="finalDate1" placeholder="Fecha final" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- Submit button -->
                                            <button type="submit" class="btn btn-primary btn-block mb-4">Consultar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="card mb-4 rounded-0 shadow border-0">
                                <div class="card-header rounded-0 bg-white border p-0 border-0">
                                    <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                                       href="#report2">
                                        <span>5 usuarios con más envíos generados en un intervalo</span> <i class="ti-plus text-primary text-right"></i>
                                    </a>
                                </div>
                                <div id="report2" class="collapse" data-parent="#accordion">
                                    <div class="card-body font-secondary text-color">
                                        <form action="#" method="POST">
                                            <!-- initialDate input -->
                                            <input type="datetime-local" name="initialDate2" id="initialDate2" placeholder="Fecha inicial" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- finalDate input -->
                                            <input type="datetime-local" name="finalDate2" id="finalDate2" placeholder="Fecha final" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- Submit button -->
                                            <button type="submit" class="btn btn-primary btn-block mb-4">Consultar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="card mb-4 rounded-0 shadow border-0">
                                <div class="card-header rounded-0 bg-white border p-0 border-0">
                                    <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                                       href="#report3">
                                        <span>5 usuarios con más pedidos generados en un intervalo</span> <i class="ti-plus text-primary text-right"></i>
                                    </a>
                                </div>
                                <div id="report3" class="collapse" data-parent="#accordion">
                                    <div class="card-body font-secondary text-color">
                                        <form action="#" method="POST">
                                            <!-- initialDate input -->
                                            <input type="datetime-local" name="initialDate3" id="initialDate3" placeholder="Fecha inicial" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- finalDate input -->
                                            <input type="datetime-local" name="finalDate3" id="finalDate3" placeholder="Fecha final" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- Submit button -->
                                            <button type="submit" class="btn btn-primary btn-block mb-4">Consultar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="card mb-4 rounded-0 shadow border-0">
                                <div class="card-header rounded-0 bg-white border p-0 border-0">
                                    <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                                       href="#report4">
                                        <span>Productos más solicitados por las tiendas en un intervalo</span> <i class="ti-plus text-primary text-right"></i>
                                    </a>
                                </div>
                                <div id="report4" class="collapse" data-parent="#accordion">
                                    <div class="card-body font-secondary text-color">
                                        <form action="#" method="POST">
                                            <!-- initialDate input -->
                                            <input type="datetime-local" name="initialDate4" id="initialDate4" placeholder="Fecha inicial" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- finalDate input -->
                                            <input type="datetime-local" name="finalDate4" id="finalDate4" placeholder="Fecha final" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- Submit button -->
                                            <button type="submit" class="btn btn-primary btn-block mb-4">Consultar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="card mb-4 rounded-0 shadow border-0">
                                <div class="card-header rounded-0 bg-white border p-0 border-0">
                                    <a class="card-link h4 d-flex tex-dark mb-0 py-3 px-4 justify-content-between" data-toggle="collapse"
                                       href="#report5">
                                        <span>Productos dañados por tienda en un intervalo de tiempo</span> <i class="ti-plus text-primary text-right"></i>
                                    </a>
                                </div>
                                <div id="report5" class="collapse" data-parent="#accordion">
                                    <div class="card-body font-secondary text-color">
                                        <form action="#" method="POST">
                                            <!-- initialDate input -->
                                            <input type="datetime-local" name="initialDate5" id="initialDate5" placeholder="Fecha inicial" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- finalDate input -->
                                            <input type="datetime-local" name="finalDate5" id="finalDate5" placeholder="Fecha final" class="form-control mb-4 shadow rounded-0"/>
                                            <!-- Submit button -->
                                            <button type="submit" class="btn btn-primary btn-block mb-4">Consultar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <!-- accordion -->
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- /reports -->

    <!-- footer -->
    <jsp:include page="../WEB-INF/footer.jsp"/>
    <!-- /footer -->

    <!-- ** JS Plugins Needed for the Project ** -->
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
