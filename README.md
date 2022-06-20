# Programme pour le grand oral de Mathématiques
Dans le cadre du Grand Oral qui représente l'épreuve finale du Baccalauréat, j'ai décidé de présenter d'un point de vue statistique à l'aide des chaînes de Markov le crash du LUNA en mai 2022. N'ayant pas les compétences pour considérer ceci d'un point de vue totalement économique, j'ai décidé de créer un modèle qui effectue des calculs basés sur les journées baissières et haussières, en considérant que les conditions pour définir une chaîne de Markov sont vérifiées (**ce qui n'est normalement pas le cas pour une telle situation**).

## Fonctionnement
Après avoir récupéré sous forme d'un tableur les valeurs de la cryptomonnaie LUNA depuis avril 2020 et jusqu'à la veille du "crash", j'ai crée un tableur simplifié avec 4 valeurs : la valeur **d'ouverture**, la valeur de **fermeture**, la valeur la plus haute atteinte et la valeur la plus basse atteinte (au sein de la fenêtre de temps de 24h considérée).
Une comparaison entre les valeurs d'ouverture et de fermeture me permet de déterminer si une "journée" est **baissière** ou **haussière**, ainsi que de calculer le % d'évolution. Par souci de réalisme, j'ai décidé d'ignorer les baisses ou les hausses de **moins de 5%** : le problème étant basé sur une **chute brutale**, des diminutions ou augmentation du prix de moins de 5% sont insignifiantes et faussent les données dans notre cas.

## Le programme
Le fichier du tableur, inclus, se prénomme **java_ods.ods**. Le programme traite toutes les valeurs une par une, détermine les données énoncées ci-dessus. Chaque ligne de valeurs fait l'objet d'un log dans la console.
Lorsque toutes les valeurs ont été traitées, un fichier PDF est généré avec la date et l'heure, contenant toutes les valeurs globales nécessaires à l'interprétation du problème en tant que chaîne de Markov. Les calculs finaux sont à réaliser à la main à l'aide de ce fichier.