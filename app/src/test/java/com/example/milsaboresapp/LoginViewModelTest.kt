import com.example.milsaboresapp.viewmodel.LoginViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LoginViewModelTest : StringSpec({

    "onCorreoChange actualiza el correo y limpia su error" {
        val vm = LoginViewModel()

        vm.onCorreoChange("correo@test.com")

        vm.estado.value.correo shouldBe "correo@test.com"
        vm.estado.value.errores.correo shouldBe null
    }

    "onClaveChange actualiza la clave y limpia su error" {
        val vm = LoginViewModel()

        vm.onClaveChange("123456")

        vm.estado.value.clave shouldBe "123456"
        vm.estado.value.errores.clave shouldBe null
    }

    "validarLogin retorna false si los campos están vacíos" {
        val vm = LoginViewModel()  // correo y clave = ""

        val resultado = vm.validarLogin()

        resultado shouldBe false
        vm.estado.value.errores.correo shouldBe "Campo requerido"
        vm.estado.value.errores.clave shouldBe "Campo requerido"
    }

    "validarLogin retorna true si los campos son válidos" {
        val vm = LoginViewModel()

        vm.onCorreoChange("correo@test.com")
        vm.onClaveChange("123456")

        val resultado = vm.validarLogin()

        resultado shouldBe true
        vm.estado.value.errores.correo shouldBe null
        vm.estado.value.errores.clave shouldBe null
    }
})
