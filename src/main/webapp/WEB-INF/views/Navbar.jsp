<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-lg" style="background-color: #e3f2fd;">
  <div class="container-fluid">
    <a class="navbar-brand">Law Harbor</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
      	
        <li class="nav-item">
          <a class="nav-link" id="homeTag" href="#">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="bookingTag">Bookings</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="caseRecordTag">Case Record</a>
        </li>
      </ul>
      <span class="navbar-text">
          <a class="btn btn-danger text-light" id="logoutTag">Logout</a>
      </span>
    </div>
  </div>
</nav>