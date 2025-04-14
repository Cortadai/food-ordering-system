A continuación vemos cómo sacar un gráfico con las dependencias del proyecto

---

1. Instalamos [graphviz](https://www.graphviz.org/download/)

2. Nos aseguramos de agregarlo al PATH

3. Dentro de una terminal con permisos de administrador corremos el siguiente comando:
```
mvn com.github.ferstl:depgraph-maven-plugin:4.0.3:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=com.food.ordering.system*:*"
```


