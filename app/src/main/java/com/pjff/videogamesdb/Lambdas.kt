package com.pjff.videogamesdb

//Video 2023-09-01 17-44-00
fun main() {
    //Paso 2.0
    operaNumeros(52,26)
    operaNumeros(30,18)

    /*
       Paso 2.2
       El unit , no regresa nada
       a y b , son los valores a los que se va mapear
    */
    val miLambdaSuma: (Int, Int) -> Unit = { a, b -> println("Segunda forma :La suma de $a + $b es: ${a+b}") }

    //Paso 2.3 Esta es otra forma
    operaNumeros(20,10, miLambdaSuma, {})

    //Recibe 2.4 parámetros e interpolamos
    operaNumeros(20,10, { a, b ->
        println("Tercera forma :La suma de $a + $b es: ${a + b}")
    }, { name ->
        println(name)
    })

    //Paso 2.5,Aquí esta la lambda
    operaNumeros(20, 10, { a,b ->
        println("La multiplicación de $a * $b es: ${a*b}")
        //Aqui mandamos a llamar a la funcion lambda
    }, { name ->
        println("Hola $name")
    })

}
/*Paso 2.1.Aquí hace una sobrecarga de números como en java,por eso podemos poner 2 funciones
con el mismo nombre*/
fun operaNumeros(num1: Int, num2: Int){
    //Interpolamos ${num1+num2}
    println("Primera forma :La suma de $num1 + $num2 es ${num1+num2}")
}

/*Paso 2.6, Operacion con Lambda -> operacion: (Int, Int) -> Unit, lambda2: (String)
le paso una funcion como un parámetro a eso operaNumeros, de tal manera que puedo mandar a llamar
a la función el unit no regresa nada */

//operacion: (Int, Int) -> Unit, le pasamos una funcion

fun operaNumeros(num1: Int, num2: Int, operacion: (Int, Int) -> Unit, lambda2: (String) -> Unit){
    operacion(num1, num2)
    lambda2("Paullie")
}