import * as vscode from "vscode"
import fetch from "cross-fetch"

interface Property {
    name: string
    type: string
    description: string
}

export function activate(context: vscode.ExtensionContext) {
    let completionItems: vscode.CompletionItem[] = []

    fetch("https://raw.githubusercontent.com/MeteorDevelopment/pulsar/master/properties.json")
        .then(res => res.json())
        .then(res => res as Property[])
        .then(res => res.forEach(property => {
            let item = new vscode.CompletionItem(property.name, vscode.CompletionItemKind.Property)

            item.insertText = property.name + ": "
            item.detail = property.type
            item.documentation = property.description

            completionItems.push(item)
        }))

    vscode.languages.registerCompletionItemProvider("pts", {
        provideCompletionItems(document, position, token, context) {
            return completionItems
        }
    })
}

export function deactivate() {}
