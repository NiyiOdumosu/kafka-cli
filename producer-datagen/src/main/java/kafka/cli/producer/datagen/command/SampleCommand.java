package kafka.cli.producer.datagen.command;

import static java.lang.System.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.concurrent.Callable;
import kafka.cli.producer.datagen.Cli;
import kafka.cli.producer.datagen.PayloadGenerator;
import picocli.CommandLine;

@CommandLine.Command(name = "sample", description = "Get a sample of the quickstart")
public class SampleCommand implements Callable<Integer> {

  @CommandLine.ArgGroup(multiplicity = "1")
  Cli.SchemaSourceOption schemaSource;

  @CommandLine.Option(
      names = {"--pretty"},
      defaultValue = "false",
      description = "Print pretty/formatted JSON")
  boolean pretty;

  final ObjectMapper json = new ObjectMapper();

  @Override
  public Integer call() throws Exception {
    final var payloadGenerator =
        new PayloadGenerator(
            new PayloadGenerator.Config(
                Optional.empty(),
                schemaSource.quickstart,
                schemaSource.schemaPath,
                1,
                PayloadGenerator.Format.JSON));

    final var sample = json.readTree(payloadGenerator.sample());
    if (pretty) {
      out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(sample));
    } else {
      out.println(json.writeValueAsString(sample));
    }
    return 0;
  }
}
