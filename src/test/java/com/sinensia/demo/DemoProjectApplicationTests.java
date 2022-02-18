package com.sinensia.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoProjectApplicationTests {

	public static final String B = "&b=";
	@Autowired TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void rootTest() {
		assertThat(restTemplate.getForObject("/", String.class)).isEqualTo("Bienvenidos a mi primera prueba");
	}

	@Test
	void helloTest() {
		assertThat(restTemplate.getForObject("/hello", String.class)).isEqualTo("Hello World!");
	}

	@Test
	void helloNameTest() {
		assertThat(restTemplate.getForObject("/hello?name=Luis", String.class)).isEqualTo("Hello Luis!");
	}

	@Test
	void helloNames() {
		String[] arr = {"Javier","Javier Arturo","Rodriguez"};
		for(String name: arr) {
			assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
					.isEqualTo("Hello "+name+"!");
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"Luis",
			"Luis Antonio",
			"Antonio",
			"Reula"
	})
	void helloParamNames(String name) {
		assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
				.isEqualTo("Hello "+name+"!");
	}

	@DisplayName("test multiple input values")
	@ParameterizedTest(name="[{index}] ({arguments}) \"{0}\" -> \"{1}\" ")
	@CsvSource({
			"a,            Hello a!",
			"b,            Hello b!",
			",             Hello null!",
			"'',           Hello World!",
			"' ',          Hello  !",
			"first+last,   Hello first last!"
	})
	void helloParamNamesCsv(String name, String expected) {
		assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
				.isEqualTo(expected);
	}

	@Test
	void canAddTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1" + B + "2", String.class)).isEqualTo("3");
	}

	@Test
	void canAddZeroTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=0" + B + "2", String.class)).isEqualTo("2");
	}

	@Test
	void canAddNegativeTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1" + B + "-2", String.class)).isEqualTo("-1");
	}

	@Test
	void canAddNullATest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=" + B + "2", String.class)).isEqualTo("2");
	}

	@Test
	void canAddNullBTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1" + B, String.class)).isEqualTo("1");
	}

	@Test
	void canAddRealTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1" + B + "2.5", String.class)).isEqualTo("3.5");
	}

	@Test
	void canAddMultiple(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1" + B + "2", String.class))
				.isEqualTo("3");
		assertThat(restTemplate.getForObject("/add?a=0" + B + "2", String.class))
				.isEqualTo("2");
		assertThat(restTemplate.getForObject("/add?a=1" + B + "-2", String.class))
				.isEqualTo("-1");
		assertThat(restTemplate.getForObject("/add?a=" + B + "2", String.class))
				.isEqualTo("2");
		assertThat(restTemplate.getForObject("/add?a=1.5" + B + "2", String.class))
				.isEqualTo("3.5");
		assertThat(restTemplate.getForObject("/add?a=1" + B, String.class))
				.isEqualTo("1");
	}


	@DisplayName("multiple additions")
	@ParameterizedTest(name="[{index}] {0} + {1} = {2}")
	@CsvSource({
			"1,   2,   3",
			"1,   1,   2",
			"1.0, 1.0, 2",
			"1,  -2,  -1",
			"1.5, 2,   3.5",
			"'',  2,   2",
			"1.5, 1.5, 3"
	})
	void canAddCsvParameterized(String a, String b, String expected) {
		assertThat(restTemplate.getForObject("/add?a="+a+ B +b, String.class))
				.isEqualTo(expected);
	}

	@Test
	void canAddExceptionJsonString() {
		assertThat(restTemplate.getForObject("/add?a=string" + B + "1", String.class).indexOf("Bad Request"))
				.isGreaterThan(-1);
	}

	@Test
	void canAddFloat() {
		assertThat(restTemplate.getForObject("/add?a=1.5" + B + "2", Float.class))
				.isEqualTo(3.5f);
	}

	@Test
	void canAddFloatException() {
		assertThrows(RestClientException.class, () -> restTemplate.getForObject("/add?a=hola" + B + "2", Float.class));
	}

	@DisplayName("multiple multiplies")
	@ParameterizedTest(name="{displayName} [{index}] {0} + {1} = {2}")
	@CsvSource({
			"1,   2,   3",
			"1,   1,   2",
			"1.0, 1.0, 2",
			"1,  -2,  -1",
			"1.5, 2,   3.5",
			"'',  2,   2",
			"1.5, 1.5, 3"
	})
	void canAddCsvParameterizedFloat(String a, String b, String expected) {
		assertThat(restTemplate.getForObject("/add?a="+a+ B +b, Float.class))
				.isEqualTo(Float.parseFloat(expected));
	}

	/* Loss-of-precision by converting Float return value into Integer
	@Test
	void canAddInteger() {
		assertThat(restTemplate.getForObject("/add?a=1.5&b=2", Integer.class))
				.isEqualTo(3.5f);
	}
	*/


	@Nested
	@DisplayName(value="Application tests")
	class AppTests {

		@Autowired
		private DemoProjectApplication app;

		@Test
		void appCanAddReturnsInteger() {
			assertThat(app.add(1f, 2f)).isEqualTo(3);
		}

		@Test
		void appCanAddReturnsFloat() {
			assertThat(app.add(1.5f, 2f)).isEqualTo(3.5f);
		}

		@Test
		void appCanAddNullReturnsFloat() {
			Exception thrown = assertThrows(NullPointerException.class, ()->{
					Float ret = (Float) app.add(null, 2f);
			});
			assertTrue(thrown.toString().contains("NullPointerException"));
			//alternatively thrown.getMessage().contains("");
		}
	}

	@Nested
	@DisplayName(value="Multiply tests")
	class MultiplyTests {
		@DisplayName("multiple multiplies")
		@ParameterizedTest(name="{displayName} [{index}] {0} * {1} = {2}")
		@CsvSource({
				"1,   2,   2",
				"1,   1,   1",
				"1.0, 1.0, 1.0",
				"1,  -2,  -2",
				"1.5, 2,   3.0",
				"'',  2,   0",
				"1.5, 1.5, 2.25"
		})
		void canMultiplyCsvParameterizedFloat(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/multiply?a="+a+ B +b, Float.class))
					.isEqualTo(Float.parseFloat(expected));
		}
	}

	@Nested
	@DisplayName(value="Subtractions tests")
	class SubtractionTests {
		@DisplayName("multiple subtractions")
		@ParameterizedTest(name="[{index}] {0} - {1} = {2}")
		@CsvSource({
				"1,   2,   -1",
				"1,   1,   0",
				"1.0, 1.0, 0",
				"1,  -2,  3",
				"1.5, 2,   -0.5",
				"'',  2,   -2",
				"1.5, 1.5, 0"
		})
		void canSubtractionCsvParameterized(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/subtraction?a="+a+ B +b, String.class))
					.isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName(value="Division tests")
	class DivisionTests {
		@DisplayName("multiple divisions")
		@ParameterizedTest(name="[{index}] {0} / {1} = {2}")
		@CsvSource({
				"1, 2, 0.50",
				"1, 1, 1.00",
				"1.0, 1.0, 1.00",
				"1,  -2,  -0.50",
				"1.5, 2,   0.75",
				"'',  2,   0.00",
				"10, 3, 3.33"
		})
		void canDivisionCsvParameterized(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/division?a="+a+ B +b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void divideByZero() {
			Exception thrown = assertThrows(RestClientException.class, ()->{
				restTemplate.getForObject("/division?a=10" + B + "0", Float.class);
			});
		}
	}

	@Nested
	@DisplayName(value="Square Root tests")
	class sqrtTests {
		@DisplayName("multiple sqrts")
		@ParameterizedTest(name="[{index}] sqrt{0} = {1}")
		@CsvSource({
				"1, 1",
				"2, 1.4",
				"25, 5",
				"'',  0.0"
		})
		void canDivisionCsvParameterized(String a, String expected) {
			assertThat(restTemplate.getForObject("/sqrt?a="+a, String.class))
					.isEqualTo(expected);
		}

		@Test
		void sqrtNegative() {
			Exception thrown = assertThrows(RestClientException.class, ()->{
				restTemplate.getForObject("/sqrt?a=-1", Float.class);
			});
		}
	}

}


