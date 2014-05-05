require 'sass'
require 'sass/engine'
require 'compass'


class SassCompilerApi

  def initialize()
    @options = Compass.sass_engine_options
    @options[:load_paths] ||= []
    @options[:load_paths].unshift(File.join(__FILE__, '../stylesheets'))
  end

  def set key, value
    @options[key.to_sym] = value
  end

  def setImporterDirectory from
    @options[:importer] = Sass::Importers::Filesystem.new(from)
  end

  def unshiftLoadpath(load_path)
    @options[:load_paths] ||= []
    @options[:load_paths].unshift(Sass::Importers::Filesystem.new(load_path))
  end

  def setHttpImagesPath path
    Compass.configuration.http_images_path = path;
  end

  def setRelativeAssets value
    Compass.configuration.relative_assets = value
  end

  def compile(inputFile, additionalOptions = {})
    opts = @options.merge(additionalOptions);
    Sass::Engine.for_file(inputFile, opts).render
  end

end