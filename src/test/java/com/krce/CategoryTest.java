package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class CategoryTest {
    @BeforeClass
    public void setUp(){
        RestAssured.baseURI="https://api.escuelajs.co/api/v1";
    }
    int id;
    @Test
    public void testcreateCategory(){
        String name="user_"+System.currentTimeMillis();
        String image="https://google.com";
        Map body= Map.of(
                "name",name,"image",image
        );
        Response response=RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body) //request
                .when()
                .post("/categories");//response
        response
                //stored in postman fromtjhat we get the log and etc...

                .then()
                .log().all()
                .statusCode(201)
                .body("name", Matchers.equalTo(name));
        id=response.jsonPath().getInt("id");
        System.out.println("crated category id"+id);

    }

    @Test(priority = 2)

    public void testGetCategory(){
        RestAssured.given()
                .pathParam("id",id)
                .when()
                .get("categories/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id",Matchers.equalTo(id));
    }

    @Test(priority = 3)
    public void testUpdaetCategory(){
        String updatedName = "Updated_" + System.currentTimeMillis();
        String updatedImage = "https://placeimg.com/640/480/any";
        Map<String, String> body = Map.of(
                "name", updatedName,
                "image", updatedImage
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/categories/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(updatedName));
    }

    @Test(priority = 4)
    public void testDeleteCategory(){
        RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete("/categories/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body(Matchers.equalTo("true"));
    }
}

