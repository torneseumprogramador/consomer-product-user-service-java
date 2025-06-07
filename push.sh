#!/bin/bash

# Check if commit message was provided
if [ -z "$1" ]
then
    read -p "Digite a mensagem do commit: " commit_message
else
    commit_message="$1"
fi

# Add all changes
git add .

# Commit with provided message
git commit -m "$commit_message"

# Pull latest changes
git pull

# Push changes
git push
