<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
	<title>${book != null ? 'Edit Book' : 'Create New Book'} - Evergreen Bookstore Administration</title>
	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<script type="text/javascript" src="../js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="../js/jquery.validate.min.js"></script>	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
		
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="..//css/richtext.min.css">	
	<script type="text/javascript" src="../js/jquery.richtext.min.js"></script>
</head>
<body>
<div class="container">
	<jsp:directive.include file="header.jsp" />
	
	<div class="row m-3">
		<div class="col text-center">
			<h2><c:out value="${book != null ? 'Edit Book' : 'Create New Book'}" /></h2>
		</div>
	</div>
	
	<c:if test="${book != null}">
		<form action="update_book" method="post" enctype="multipart/form-data">
		<input type="hidden" name="bookId" value="${book.bookId}">
	</c:if>
	<c:if test="${book == null}">
		<form action="create_book" method="post" enctype="multipart/form-data">
	</c:if>
	
		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Category:</label>
			<div class="col-sm-8">
				<select name="category" class="form-control" required>
					<c:forEach items="${listCategory}" var="category">
						<c:if test="${category.categoryId eq book.category.categoryId}">
							<option value="${category.categoryId}" selected>
						</c:if>
						<c:if test="${category.categoryId ne book.category.categoryId}">
							<option value="${category.categoryId}">
						</c:if>							
							${category.name}
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Title:</label>
			<div class="col-sm-8">
				<input type="text" name="title" size="20" value="${book.title}" class="form-control" required />
			</div>
		</div>

		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Author:</label>
			<div class="col-sm-8">
				<input type="text" name="author" size="20" value="${book.author}" class="form-control" required />
			</div>
		</div>

		<div class="form-group row">
			<label class="col-sm-4 col-form-label">ISBN:</label>
			<div class="col-sm-8">
				<input type="text" name="isbn" size="20" value="${book.isbn}" class="form-control" required />
			</div>
		</div>
				
		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Publish Date:</label>
			<div class="col-sm-8">
				<input type="date" name="publishDate" size="20" required class="form-control"
					value="<fmt:formatDate pattern='yyyy-MM-dd' value='${book.publishDate}' />" />				
			</div>
		</div>
		
		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Book Image:</label>
			<div class="col-sm-8">
				<c:if test="${book == null}">
				<input type="file" id="bookImage" name="bookImage" size="20" required /><br/>
				</c:if>
				
				<c:if test="${book != null}">
				<input type="file" id="bookImage" name="bookImage" size="20" /><br/>
				</c:if>					
				
				<img id="thumbnail" alt="Image Preview" style="width:20%; margin-top: 10px"
					src="data:image/jpg;base64,${book.base64Image}"
				 />				
			</div>
		</div>
		
		<div class="form-group row">
			<label class="col-sm-4 col-form-label">Price:</label>
			<div class="col-sm-8">
				<input type="text" name="price" id="price" size="20" value="${book.price}" class="form-control" required />
			</div>
		</div>
		
		<div class="row">
			<label class="col-sm-4 col-form-label">Description:</label>
		</div>
		
		<div class="row m-2">
			<textarea rows="5" cols="50" name="description" id="description" required>${book.description}</textarea>
		</div>
		
		<div class="row m-4">
			<div class="col text-center">
				<button type="submit" class="btn btn-primary mr-2">Save</button>
				<button type="button" class="btn btn-secondary ml-2" onclick="history.go(-1);">Cancel</button>
			</div>
		</div>
	</form>

	<jsp:directive.include file="footer.jsp" />
</div>	
</body>
<script type="text/javascript">

	$(document).ready(function() {
		$('#description').richText();
		
		$('#bookImage').change(function() {
			showImageThumbnail(this);
		});
		
	});
	
	function showImageThumbnail(fileInput) {
		var file = fileInput.files[0];
		
		var reader = new FileReader();
		
		reader.onload = function(e) {
			$('#thumbnail').attr('src', e.target.result);
		};
		
		reader.readAsDataURL(file);
	}
</script>
</html>