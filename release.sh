#!/bin/bash

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored messages
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if version argument is provided
if [ -z "$1" ]; then
    print_error "バージョン番号を指定してください"
    echo "使い方: ./release.sh <version>"
    echo "例: ./release.sh 3.2.0"
    exit 1
fi

VERSION=$1
TAG_NAME="v${VERSION}"

# Validate version format (X.Y.Z)
if ! [[ $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    print_error "バージョン番号のフォーマットが正しくありません (X.Y.Z形式で指定してください)"
    echo "例: 3.2.0"
    exit 1
fi

print_info "バージョン ${TAG_NAME} のリリースを開始します"

# Check if we're on main branch
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    print_warn "現在のブランチは '${CURRENT_BRANCH}' です"
    read -p "mainブランチに切り替えますか? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git checkout main
        git pull origin main
    else
        print_error "mainブランチでリリースを作成してください"
        exit 1
    fi
fi

# Check if working directory is clean
if [ -n "$(git status --porcelain)" ]; then
    print_error "作業ディレクトリに未コミットの変更があります"
    git status --short
    exit 1
fi

# Update version in build.gradle.kts
print_info "build.gradle.kts のバージョンを更新中..."
BUILD_FILE="build.gradle.kts"

# Create temporary file for sed operation
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' "s/^version = \".*\"/version = \"${VERSION}\"/" "$BUILD_FILE"
    sed -i '' "s/packageVersion = \".*\"/packageVersion = \"${VERSION}\"/" "$BUILD_FILE"
else
    # Linux
    sed -i "s/^version = \".*\"/version = \"${VERSION}\"/" "$BUILD_FILE"
    sed -i "s/packageVersion = \".*\"/packageVersion = \"${VERSION}\"/" "$BUILD_FILE"
fi

# Verify the changes
if grep -q "version = \"${VERSION}\"" "$BUILD_FILE"; then
    print_info "バージョンを ${VERSION} に更新しました"
else
    print_error "バージョンの更新に失敗しました"
    exit 1
fi

# Commit the version change
print_info "変更をコミット中..."
git add "$BUILD_FILE"
if git diff --cached --quiet; then
    print_warn "バージョンは既に ${VERSION} です。コミットをスキップします"
else
    git commit -m "chore: bump version to ${TAG_NAME}

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>"
fi

# Create and push tag
print_info "タグ ${TAG_NAME} を作成中..."
git tag -a "$TAG_NAME" -m "$TAG_NAME"

print_info "変更とタグをプッシュ中..."
git push origin main
git push origin "$TAG_NAME"

# Generate release notes from recent commits
print_info "リリースノートを生成中..."

# Get commits since last tag
LAST_TAG=$(git tag --sort=-v:refname | grep -v "$TAG_NAME" | head -1)
if [ -n "$LAST_TAG" ]; then
    print_info "前回のタグ: ${LAST_TAG}"
    COMMITS=$(git log ${LAST_TAG}..${TAG_NAME} --oneline --no-merges)
else
    print_info "これが最初のリリースです"
    COMMITS=$(git log ${TAG_NAME} --oneline --no-merges | head -20)
fi

# Create draft release
print_info "GitHubリリースを作成中..."
gh release create "$TAG_NAME" \
    --title "$TAG_NAME" \
    --draft \
    --notes "## 🚀 What's New

<!-- ここにリリースノートを記述してください -->

### Recent Commits
\`\`\`
${COMMITS}
\`\`\`

---
*Note: このリリースはドラフトとして作成されています。GitHub Actionsでビルドが完了したら、バイナリファイルが自動的にアップロードされます。ビルド完了後にリリースを公開してください。*

**Full Changelog**: https://github.com/kaleidot725/adbpad/compare/${LAST_TAG}...${TAG_NAME}"

print_info "✅ リリースの準備が完了しました!"
echo ""
print_info "次のステップ:"
echo "  1. GitHub Actionsでビルドが完了するまで待つ"
echo "  2. リリースページでリリースノートを編集"
echo "  3. ドラフトリリースを公開"
echo ""
print_info "リリースページ: https://github.com/kaleidot725/adbpad/releases/tag/${TAG_NAME}"
