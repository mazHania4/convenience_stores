
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Módulo de supervisión</title>
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
<header class="banner overlay bg-cover" data-background="${pageContext.request.contextPath}/images/supervisorbanner.png">
    <!-- navBar -->
    <jsp:include page="../WEB-INF/navBar.jsp"/>
    <!-- /navBar -->
    <!-- banner -->
    <div class="container section">
        <div class="row">
            <div class="col-lg-8 text-center mx-auto">
                <h1 class="text-white mb-3">Módulo de supervisión</h1>
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
                <h2 class="section-title">Opciones</h2>
            </div>
            <div class="col-lg-4 col-sm-6 mb-4">
                <a href="#" class="px-4 py-5 bg-white shadow text-center d-block match-height">
                    <i class="ti-view-list-alt icon text-primary d-block mb-4"></i>
                    <h3 class="mb-3 mt-0">Revisar pedido</h3>
                    <p class="mb-0">Aprobar o rechazar un pedido</p>
                </a>
            </div>

        </div>
    </div>
</section>
<!-- /options -->

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
