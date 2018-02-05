# Trainer notes

# Linux docker nodes

Provision the docker machines using the [praqma-training/train-infra](https://github.com/praqma-training/train-infra) repo.

Things to remember:

 - Apply the tag "Status" value "Prod" to ensure the machines are not killed by the lamda cleanup script
 - Change the firewall rules to open access from everywhere

# Windows docker nodes

Provision The windows machines using the [praqma/provision-win-docker](https://github.com/Praqma/provision-win-docker) repo.

