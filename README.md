# Le Canul

* Charles-Antoine Sohier
* Thibaud Vouillon

Nous avons décidé de développer une application pour un site internet qui a été créé cet été. Le site internet se présente sous la forme d'un journal où l'on peut lire des articles et poster des commentaires.

## Fonctionnalités

Les différentes fonctionnalités implémentées dans le projet sont les suivantes:

* Affichage de la liste des articles
* Affichage d'un article accompagné de sa liste de commentaire
* Création de commentaire
* Login via Facebook

Nous souhaiterions également développé les fonctionnalités suivantes dans un avenir proche:

* Notification et badges de nouvel article non lu
* Login et Signin via l'app
* Affichages des commentaires par utilisateur

## Points forts

Les points forts de cette application sont les suivants:

* L'affichage des extraits d'article se fait à l'aide de RecyclerView
* L'affichage d'un article et de ses commentaires se fait également à l'aide d'un RecyclerView, cependant celui-ci traite les données de manière differente selon leur type.
* L'affichage des extraits d'article se fait à l'aide d'un EndlessScroll ce qui permet de s'affranchir de la pagination
* De même pour l'affichage des commentaires
* L'authentification auprès de l'API se fait à l'aide de challenge
* Très peu d'actions sont nécessaires pour lire un article et ses commentaires


## Points faibles

Certaines fonctionnalités ne sont pas présentes:

* Le login via facebook n'est pas utilisé
* Les connexions à l'API ne sont pas en https
* Il n'y a pas de systeme d'utilisateur pour le moment
* Les images ne sont pas traitées du tout
* Les animations entre les pages n'ont pas toutes été faites
* 
