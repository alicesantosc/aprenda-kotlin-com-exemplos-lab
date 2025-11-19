// [Template no Kotlin Playground](https://pl.kotl.in/WcteahpyN)

import java.time.LocalDateTime

// =======================
// MODELOS BÁSICOS
// =======================

enum class Nivel { BASICO, INTERMEDIARIO, DIFICIL }

// NOVO: tipo de conteúdo
enum class TipoConteudo { VIDEO, ARTIGO, QUIZ, PROJETO }

data class Usuario(
    val id: Long = 0,
    val nome: String,
    val email: String,
    val dataCadastro: LocalDateTime = LocalDateTime.now()
)

data class ConteudoEducacional(
    var nome: String,
    val duracao: Int = 60, // duração em minutos
    val tipo: TipoConteudo = TipoConteudo.VIDEO // NOVO
)

data class Formacao(
    val nome: String,
    val nivel: Nivel,
    var conteudos: List<ConteudoEducacional> = emptyList(),
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    val capacidadeMaxima: Int = Int.MAX_VALUE // NOVO: limite de vagas
) {

    private val inscritos = mutableListOf<Usuario>()

    // =======================
    // MATRÍCULA
    // =======================

    fun possuiVagas(): Boolean = inscritos.size < capacidadeMaxima

    fun matricular(usuario: Usuario) {
        when {
            usuario in inscritos -> {
                println("✗ ${usuario.nome} já está matriculado na formação '$nome'")
            }
            !possuiVagas() -> {
                println("✗ Formação '$nome' está com todas as vagas preenchidas (${capacidadeMaxima} vagas).")
            }
            else -> {
                inscritos.add(usuario)
                println("✓ ${usuario.nome} foi matriculado na formação '$nome'")
            }
        }
    }

    fun matricularVarios(usuarios: List<Usuario>) {
        usuarios.forEach { matricular(it) }
    }

    fun removerMatricula(usuario: Usuario): Boolean {
        return if (inscritos.remove(usuario)) {
            println("✓ ${usuario.nome} foi removido da formação '$nome'")
            true
        } else {
            println("✗ ${usuario.nome} não estava matriculado na formação '$nome'")
            false
        }
    }

    // NOVO: checar se usuário está matriculado
    fun estaMatriculado(usuario: Usuario): Boolean = usuario in inscritos

    fun obterDuracaoTotal(): Int {
        return conteudos.sumOf { it.duracao }
    }

    fun obterTotalInscritos(): Int {
        return inscritos.size
    }

    // NOVO: quantas vagas ainda tem
    fun obterVagasRestantes(): Int = (capacidadeMaxima - inscritos.size).coerceAtLeast(0)

    // =======================
    // LISTAGENS DE INSCRITOS
    // =======================

    fun listarInscritos() {
        if (inscritos.isEmpty()) {
            println("Nenhum aluno matriculado")
            return
        }
        println("Alunos matriculados:")
        inscritos.forEach { println("  - ${it.nome} (${it.email})") }
    }

    // NOVO: inscritos ordenados por nome
    fun listarInscritosOrdenadosPorNome() {
        if (inscritos.isEmpty()) {
            println("Nenhum aluno matriculado")
            return
        }
        println("Alunos matriculados (ordenados por nome):")
        inscritos
            .sortedBy { it.nome.lowercase() }
            .forEach { println("  - ${it.nome} (${it.email})") }
    }

    // NOVO: inscritos ordenados por data de cadastro
    fun listarInscritosPorDataCadastro() {
        if (inscritos.isEmpty()) {
            println("Nenhum aluno matriculado")
            return
        }
        println("Alunos matriculados (por data de cadastro):")
        inscritos
            .sortedBy { it.dataCadastro }
            .forEach { println("  - ${it.nome} (${it.email}) - cadastrado em ${it.dataCadastro}") }
    }

    // NOVO: buscar inscrito pelo e-mail
    fun buscarInscritoPorEmail(email: String): Usuario? {
        return inscritos.find { it.email.equals(email, ignoreCase = true) }
    }

    // =======================
    // CONTEÚDOS
    // =======================

    // NOVO: listar conteúdos por tipo
    fun listarConteudosPorTipo(tipoConteudo: TipoConteudo) {
        val filtrados = conteudos.filter { it.tipo == tipoConteudo }
        if (filtrados.isEmpty()) {
            println("Nenhum conteúdo do tipo $tipoConteudo encontrado na formação '$nome'.")
            return
        }
        println("Conteúdos do tipo $tipoConteudo na formação '$nome':")
        filtrados.forEach {
            println("  📚 ${it.nome} - ${it.duracao} min")
        }
    }

    // NOVO: listar conteúdos ordenados por duração
    fun listarConteudosOrdenadosPorDuracao() {
        if (conteudos.isEmpty()) {
            println("Nenhum conteúdo cadastrado na formação '$nome'.")
            return
        }
        println("Conteúdos ordenados por duração na formação '$nome':")
        conteudos
            .sortedBy { it.duracao }
            .forEach {
                println("  📚 ${it.nome} - ${it.duracao} min (${it.tipo})")
            }
    }

    // =======================
    // DETALHES
    // =======================

    fun exibirDetalhes() {
        println("\n╔════════════════════════════════════════╗")
        println("║        DETALHES DA FORMAÇÃO            ║")
        println("╚════════════════════════════════════════╝")
        println("Nome: $nome")
        println("Nível: $nivel")
        println("Duração Total: ${obterDuracaoTotal()} minutos (${obterDuracaoTotal() / 60}h)")
        println("Total de Inscritos: ${obterTotalInscritos()}")
        println("Capacidade Máxima: $capacidadeMaxima")
        println("Vagas Restantes: ${obterVagasRestantes()}")
        println("Data de Criação: $dataCriacao")
        println("\nConteúdos Educacionais:")
        if (conteudos.isEmpty()) {
            println("  Nenhum conteúdo cadastrado ainda.")
        } else {
            conteudos.forEach {
                println("  📚 ${it.nome} - ${it.duracao} min (${it.tipo})")
            }
        }
        println()
    }
}

// =======================
// EXTENSÕES ÚTEIS
// =======================

// NOVO: carga horária total de uma lista de formações
fun List<Formacao>.cargaHorariaTotal(): Int =
    this.sumOf { it.obterDuracaoTotal() }

// NOVO: agrupar formações por nível e exibir um resumo
fun List<Formacao>.exibirResumoPorNivel() {
    if (this.isEmpty()) {
        println("Nenhuma formação cadastrada.")
        return
    }

    println("\n📊 RESUMO DE FORMAÇÕES POR NÍVEL")
    this.groupBy { it.nivel }
        .forEach { (nivel, formacoes) ->
            val totalFormacoes = formacoes.size
            val totalInscritos = formacoes.sumOf { it.obterTotalInscritos() }
            val cargaTotal = formacoes.sumOf { it.obterDuracaoTotal() }

            println("\nNível: $nivel")
            println("  - Formações: $totalFormacoes")
            println("  - Inscritos: $totalInscritos")
            println("  - Carga horária total: $cargaTotal min (${cargaTotal / 60}h)")
        }
}

// =======================
// MAIN
// =======================

fun main() {
    println("🎓 SISTEMA DE FORMAÇÕES - DIO\n")

    // ===== CRIANDO USUÁRIOS =====
    val usuario1 = Usuario(
        id = 1,
        nome = "João Silva",
        email = "joao.silva@email.com"
    )

    val usuario2 = Usuario(
        id = 2,
        nome = "Maria Santos",
        email = "maria.santos@email.com"
    )

    val usuario3 = Usuario(
        id = 3,
        nome = "Pedro Costa",
        email = "pedro.costa@email.com"
    )

    val usuario4 = Usuario(
        id = 4,
        nome = "Ana Oliveira",
        email = "ana.oliveira@email.com"
    )

    println("✓ ${usuario1.nome} cadastrado")
    println("✓ ${usuario2.nome} cadastrado")
    println("✓ ${usuario3.nome} cadastrado")
    println("✓ ${usuario4.nome} cadastrado\n")

    // ===== CRIANDO CONTEÚDOS EDUCACIONAIS =====
    val conteudo1 = ConteudoEducacional("Introdução ao Kotlin", 120, TipoConteudo.VIDEO)
    val conteudo2 = ConteudoEducacional("Programação Orientada a Objetos", 150, TipoConteudo.ARTIGO)
    val conteudo3 = ConteudoEducacional("Banco de Dados em Kotlin", 180, TipoConteudo.VIDEO)
    val conteudo4 = ConteudoEducacional("API REST com Kotlin", 200, TipoConteudo.PROJETO)
    val conteudo5 = ConteudoEducacional("Quiz de Fundamentos Kotlin", 30, TipoConteudo.QUIZ)

    println("✓ Conteúdo: '${conteudo1.nome}' criado")
    println("✓ Conteúdo: '${conteudo2.nome}' criado")
    println("✓ Conteúdo: '${conteudo3.nome}' criado")
    println("✓ Conteúdo: '${conteudo4.nome}' criado")
    println("✓ Conteúdo: '${conteudo5.nome}' criado\n")

    // ===== CRIANDO FORMAÇÕES =====
    val formacaoKotlinBasico = Formacao(
        nome = "Kotlin para Iniciantes",
        nivel = Nivel.BASICO,
        conteudos = listOf(conteudo1, conteudo2, conteudo5),
        capacidadeMaxima = 2 // NOVO: limite pequeno pra testar
    )

    val formacaoKotlinAvancado = Formacao(
        nome = "Kotlin Avançado e Backend",
        nivel = Nivel.DIFICIL,
        conteudos = listOf(conteudo3, conteudo4),
        capacidadeMaxima = 3
    )

    val formacaoKotlinIntermediario = Formacao(
        nome = "Kotlin Intermediário",
        nivel = Nivel.INTERMEDIARIO,
        conteudos = listOf(conteudo2, conteudo3),
        capacidadeMaxima = 5
    )

    val todasFormacoes = listOf(
        formacaoKotlinBasico,
        formacaoKotlinAvancado,
        formacaoKotlinIntermediario
    )

    println("✓ Formação: '${formacaoKotlinBasico.nome}' criada")
    println("✓ Formação: '${formacaoKotlinAvancado.nome}' criada")
    println("✓ Formação: '${formacaoKotlinIntermediario.nome}' criada\n")

    // ===== CENÁRIOS DE TESTE =====

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 1: Matrícula Individual")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinBasico.matricular(usuario1)
    formacaoKotlinBasico.matricular(usuario2)

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 2: Tentativa de Matrícula Duplicada")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinBasico.matricular(usuario1) // Tentará matricular novamente

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 3: Capacidade Máxima Atingida")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinBasico.matricular(usuario3) // Deve falhar por falta de vagas

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 4: Matrícula em Lote")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinAvancado.matricularVarios(
        listOf(usuario1, usuario3, usuario4)
    )

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 5: Visualizar Detalhes das Formações")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinBasico.exibirDetalhes()
    formacaoKotlinAvancado.exibirDetalhes()

    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 6: Listar Inscritos por Formação (Ordenados)")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("\n${formacaoKotlinBasico.nome}:")
    formacaoKotlinBasico.listarInscritosOrdenadosPorNome()

    println("\n${formacaoKotlinAvancado.nome}:")
    formacaoKotlinAvancado.listarInscritosPorDataCadastro()

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 7: Buscar Inscrito por E-mail")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    val buscaEmail = "joao.silva@email.com"
    val encontrado = formacaoKotlinAvancado.buscarInscritoPorEmail(buscaEmail)
    if (encontrado != null) {
        println("✓ Encontrado: ${encontrado.nome} está matriculado em '${formacaoKotlinAvancado.nome}'")
    } else {
        println("✗ Nenhum inscrito com e-mail $buscaEmail em '${formacaoKotlinAvancado.nome}'")
    }

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 8: Listar Conteúdos por Tipo")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinBasico.listarConteudosPorTipo(TipoConteudo.VIDEO)
    formacaoKotlinBasico.listarConteudosPorTipo(TipoConteudo.QUIZ)

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 9: Conteúdos Ordenados por Duração")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    formacaoKotlinAvancado.listarConteudosOrdenadosPorDuracao()

    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 10: Estatísticas Finais")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("${formacaoKotlinBasico.nome}:")
    println("  Total de inscritos: ${formacaoKotlinBasico.obterTotalInscritos()}")
    println("  Duração total: ${formacaoKotlinBasico.obterDuracaoTotal()} minutos")
    println("  Vagas restantes: ${formacaoKotlinBasico.obterVagasRestantes()}")

    println("\n${formacaoKotlinAvancado.nome}:")
    println("  Total de inscritos: ${formacaoKotlinAvancado.obterTotalInscritos()}")
    println("  Duração total: ${formacaoKotlinAvancado.obterDuracaoTotal()} minutos")
    println("  Vagas restantes: ${formacaoKotlinAvancado.obterVagasRestantes()}")

    println("\n${formacaoKotlinIntermediario.nome}:")
    println("  Total de inscritos: ${formacaoKotlinIntermediario.obterTotalInscritos()}")
    println("  Duração total: ${formacaoKotlinIntermediario.obterDuracaoTotal()} minutos")
    println("  Vagas restantes: ${formacaoKotlinIntermediario.obterVagasRestantes()}")

    // NOVO: uso das extensões em lista de formações
    println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("CENÁRIO 11: Resumo Geral das Formações")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    todasFormacoes.exibirResumoPorNivel()
    val cargaTotal = todasFormacoes.cargaHorariaTotal()
    println("\nCarga horária total de todas as formações: $cargaTotal min (${cargaTotal / 60}h)")

    println("\n✓ Simulação concluída com sucesso!")
}


