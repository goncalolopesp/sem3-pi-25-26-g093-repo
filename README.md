# Projeto Integrador — 3.º Semestre LEI 2025/26 (G093)

Este é o Projeto Integrador do 3.º semestre da Licenciatura em Engenharia Informática do ISEP. Trata da **logística de uma rede ferroviária europeia**: armazéns nas estações (descarga de vagões, baías, encomendas e devoluções), planeamento e despacho de carga, montagem de comboios e análise de grafos da rede (backbone mínimo, hubs, caminhos mais curtos com risco). Junta várias unidades curriculares:

| UC | Onde está | O quê |
|---|---|---|
| ESINF | `src/main/java` (Java) | Estruturas de dados e algoritmos: armazém, árvores de pesquisa de estações, grafos (USEI) |
| LAPR3 | `src/main/java` (Java) | Operações de carga, tempos de viagem, planeamento/despacho de carga (USPL) |
| BDDAD | `sql_scripts/` | Modelo da BD Oracle, triggers, funções e testes (USBD) |
| ARQCP | `stationMngt/`, `doc/US/arqcp-us/` | Gestão de estação em C + Assembly RISC-V, com sensores em Arduino (USAC) |

Equipa: Tomás Pinho (1240731), Gonçalo Afonso (1240674), Gonçalo Pinto (1241232), José Cardoso (1241323) e Rafael Capão (1241285).

## Estrutura

```
src/main/java/pt/ipp/isep/dei/pi25_26/g093/app/
  Main.java, Bootstrap.java   ponto de entrada (consola)
  domain/ controller/ service/ repositories/ ui/   camadas da aplicação
  graphs/ datastructures/ simulation/              algoritmos de ESINF
src/test/java/                testes unitários (JUnit 4)
train_station_dataset/        CSV de entrada (estações, linhas, vagões, encomendas...)
generated-graphs/             grafos gerados pelo programa (.dot / .svg)
sql_scripts/                  scripts SQL por US (create/inserts, triggers, funções, testes)
stationMngt/                  módulo C/Assembly RISC-V (Makefile) + sketch Arduino
doc/                          documentação (US, modelo de domínio, complexidade temporal)
SCRUM/                        sprint backlogs e burndown
```

## Compilar e executar a aplicação Java

Requisitos: **JDK 17 ou superior** e **Maven 3**. Corre os comandos a partir da raiz do repositório.

### 1. Compilar

```bash
mvn clean package -DskipTests
```

### 2. Executar

```bash
mvn exec:java
```

No arranque, o programa pergunta como carregar os CSV:
- `a` (automático) carrega tudo o que está em `train_station_dataset/`;
- `m` (manual) pede o caminho de cada ficheiro.

Depois aparece o menu principal:

```
1. Team Presentation                    7. Directed Line Upgrade Plan (USEI11)
2. Cargo Handling Operations (USPL02)   8. Minimal Backbone (USEI12)
3. Travel Time Between Stations (USPL03) 9. Rail Hub Score (USEI13)
4. Freight Planner (USPL08)            10. Hub Score (USEI14)
5. Assemble Train (USPL09)             11. Risk Aware Shortest Path (USEI15)
6. Freight Dispatcher (USPL10)          0. Exit
```

> As opções **3, 4, 5 e 6** ligam-se à base de dados Oracle do ISEP (`vsgate-s1.dei.isep.ipp.pt`), configurada em `utilities/DatabaseConnection.java`. Só funcionam dentro da rede do ISEP (ou por VPN) e com a BD ligada. As restantes opções só usam os CSV.

### 3. Testes e cobertura

```bash
mvn clean test               # testes unitários
mvn test jacoco:report       # relatório de cobertura (target/site/jacoco)
```

## Base de dados (BDDAD)

Os scripts estão em `sql_scripts/`, um por User Story. Para criar a BD de raiz num Oracle (por exemplo no SQL Developer ou no SQL*Plus), corre por esta ordem:

1. `sql_scripts/USBD03/SEM3_PI_G093 Create.ddl`
2. `sql_scripts/USBD03/SEM3_PI_G093 Inserts.ddl`
3. Os scripts de cada US que quiseres testar (`*_Trigger.sql` / `*_Function.sql`, depois `*_UnitTests.sql`)

## Módulo de gestão da estação (ARQCP)

O código C e Assembly está em `stationMngt/` e é compilado para RISC-V 32 bits. Precisa da toolchain `riscv32-linux-gcc` e do `qemu-riscv32`. O `QEMU_SYSROOT` está definido no `Makefile` e tem de ser ajustado ao teu sistema.

```bash
cd stationMngt
make        # compila stationMngt.elf
make run    # executa no QEMU
make clean
```

O sketch do Arduino (sensor DHT11 e LEDs dos semáforos das linhas) está em `stationMngt/arduino/dth11_leds/` e abre-se no Arduino IDE.
